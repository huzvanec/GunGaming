package cz.jeme.programu.gungaming.manager;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.util.Message;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ZoomManager {
    INSTANCE;

    private final @NotNull Map<Player, ItemStack> helmetItems = new HashMap<>();
    private static final @NotNull ItemStack PUMPKIN = new ItemStack(Material.CARVED_PUMPKIN);
    private static final @NotNull PotionEffect NIGHT_VISION = new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 255, true, false, false);

    static {
        PUMPKIN.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
        PUMPKIN.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
        ItemMeta meta = PUMPKIN.getItemMeta();
        List<Component> lore = new ArrayList<>();
        assert meta != null;
        meta.lore(lore);
        meta.displayName(Message.EMPTY);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setCustomModelData(1);
        PUMPKIN.setItemMeta(meta);
    }

    public void nextZoom(@NotNull Player player, double multiplier) {
        if (!helmetItems.containsKey(player) || helmetItems.get(player).equals(PUMPKIN)) {
            zoomIn(player, multiplier);
            return;
        }
        zoomOut(player);
    }

    public void zoomIn(@NotNull Player player, double multiplier) {
        if (helmetItems.containsKey(player) && !helmetItems.get(player).equals(PUMPKIN)) {
            return;
        }
        if (player.isFlying()) {
            player.sendActionBar(Message.from("<red>You can't use scope while flying!</red>"));
            return;
        }
        setZoom(player, multiplier);
        Bukkit.getScheduler().runTaskLater(GunGaming.getPlugin(), () -> player.addPotionEffect(NIGHT_VISION), 1L);
        showScope(player);
    }

    public void zoomOut(@NotNull Player player) {
        if (!helmetItems.containsKey(player)) {
            return;
        }
        if (helmetItems.get(player).equals(PUMPKIN)) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(GunGaming.getPlugin(), () -> player.removePotionEffect(PotionEffectType.NIGHT_VISION), 1L);
        setZoom(player, 1);
        hideScope(player);
    }

    public void setZoom(@NotNull Player player, double multiplier) {
        Abilities abilities = new Abilities();
        abilities.walkingSpeed = calcZoom(multiplier);
        // Modifying walking speed in ability NMS packet causes bad behavior to flying!

        Packets.sendPacket(player, new ClientboundPlayerAbilitiesPacket(abilities));
        player.sendActionBar(Message.from("<gold>zoom: </gold><aqua>" + multiplier + "×</aqua>"));
    }

    private float calcZoom(double multiplier) {
        if (multiplier < 1 || multiplier > 10) {
            throw new IllegalArgumentException("1.0 - 10.0 expected, got " + multiplier);
        }
        return (float) (1.0 / (20 / multiplier - 10));
    }

    private void showScope(@NotNull Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack helmet = inventory.getHelmet();
        if (helmet == null) {
            helmet = new ItemStack(Material.AIR);
        }
        helmetItems.put(player, helmet);
        inventory.setHelmet(PUMPKIN);
    }

    private void hideScope(@NotNull Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.setHelmet(helmetItems.get(player));
        helmetItems.put(player, PUMPKIN);
    }

    public void zoomOutAll() {
        for (Player player : helmetItems.keySet()) {
            zoomOut(player);
        }
    }
}
