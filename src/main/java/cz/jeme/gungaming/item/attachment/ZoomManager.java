package cz.jeme.gungaming.item.attachment;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.util.Components;
import cz.jeme.gungaming.util.Packets;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.world.entity.player.Abilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;

@NullMarked
public enum ZoomManager {
    INSTANCE;

    private static final ItemStack PUMPKIN = ItemStack.of(Material.CARVED_PUMPKIN);
    private static final PotionEffect NIGHT_VISION = new PotionEffect(
            PotionEffectType.NIGHT_VISION,
            -1,
            255,
            true,
            false,
            false
    );

    static {
        PUMPKIN.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
        PUMPKIN.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
        PUMPKIN.editMeta(meta -> {
            meta.displayName(Component.empty());
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        });
        //noinspection UnstableApiUsage
        PUMPKIN.setData(DataComponentTypes.ITEM_MODEL, GunGaming.key("scope_overlay"));
    }

    private final Map<UUID, @Nullable ItemStack> helmetItems = new HashMap<>();
    private final Map<UUID, @Nullable PotionEffect> nightVisions = new HashMap<>();
    private final Set<UUID> zoomedIn = new HashSet<>();

    public void zoomIn(final Player player, final double zoom) {
        final UUID uuid = player.getUniqueId();
        if (zoomedIn.contains(uuid)) return;
        if (player.isFlying()) {
            player.sendActionBar(Components.of("<red>You can't use scope while flying!"));
            return;
        }
        setZoom(player, zoom);
        nightVisions.put(uuid, player.getPotionEffect(PotionEffectType.NIGHT_VISION));
        Bukkit.getScheduler().runTaskLater(
                GunGaming.instance(),
                () -> player.addPotionEffect(NIGHT_VISION),
                1L
        );
        final PlayerInventory inventory = player.getInventory();
        helmetItems.put(uuid, inventory.getHelmet());
        inventory.setHelmet(PUMPKIN);
        zoomedIn.add(uuid);
    }

    public void zoomOut(final Player player) {
        final UUID uuid = player.getUniqueId();
        if (!zoomedIn.contains(uuid)) return;
        setZoom(player, 1);
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        final PotionEffect nightVision = nightVisions.get(uuid);
        if (nightVision != null)
            player.addPotionEffect(nightVision);
        player.getInventory().setHelmet(helmetItems.remove(uuid));
        zoomedIn.remove(uuid);
    }

    public void zoomOutAll() {
        for (final UUID uuid : zoomedIn) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null)
                throw new RuntimeException("Zoomed-in player is null!");
            zoomOut(player);
        }
    }

    public void nextZoom(final Player player, final double zoom) {
        final UUID uuid = player.getUniqueId();
        if (zoomedIn.contains(uuid)) zoomOut(player);
        else zoomIn(player, zoom);
    }

    private void setZoom(final Player player, final double zoom) {
        final Abilities abilities = new Abilities();
        abilities.setWalkingSpeed(calcZoom(zoom));
        Packets.send(player, new ClientboundPlayerAbilitiesPacket(abilities));
    }

    private static float calcZoom(final double zoom) {
        if (zoom < 1 || zoom > 10)
            throw new IllegalArgumentException("Zoom must be between 1 and 10!");
        return (float) (1D / (20 / zoom - 10));
    }
}
