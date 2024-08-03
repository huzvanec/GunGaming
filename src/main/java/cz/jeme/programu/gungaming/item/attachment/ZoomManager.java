package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.util.Components;
import cz.jeme.programu.gungaming.util.Packets;
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
import org.jetbrains.annotations.NotNull;

import java.util.*;

public enum ZoomManager {
    INSTANCE;

    private static final @NotNull ItemStack PUMPKIN = ItemStack.of(Material.CARVED_PUMPKIN);
    private static final @NotNull PotionEffect NIGHT_VISION = new PotionEffect(
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
            meta.setCustomModelData(1);
        });
    }

    private final @NotNull Map<UUID, ItemStack> helmetItems = new HashMap<>();
    private final @NotNull Map<UUID, PotionEffect> nightVisions = new HashMap<>();
    private final @NotNull Set<UUID> zoomedIn = new HashSet<>();

    public void zoomIn(final @NotNull Player player, final double zoom) {
        final UUID uuid = player.getUniqueId();
        if (zoomedIn.contains(uuid)) return;
        if (player.isFlying()) {
            player.sendActionBar(Components.of("<red>You can't use scope while flying!"));
            return;
        }
        setZoom(player, zoom);
        nightVisions.put(uuid, player.getPotionEffect(PotionEffectType.NIGHT_VISION));
        Bukkit.getScheduler().runTaskLater(
                GunGaming.plugin(),
                () -> player.addPotionEffect(NIGHT_VISION),
                1L
        );
        final PlayerInventory inventory = player.getInventory();
        helmetItems.put(uuid, inventory.getHelmet());
        inventory.setHelmet(PUMPKIN);
        zoomedIn.add(uuid);
    }

    public void zoomOut(final @NotNull Player player) {
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

    public void nextZoom(final @NotNull Player player, final double zoom) {
        final UUID uuid = player.getUniqueId();
        if (zoomedIn.contains(uuid)) zoomOut(player);
        else zoomIn(player, zoom);
    }

    private void setZoom(final @NotNull Player player, final double zoom) {
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
