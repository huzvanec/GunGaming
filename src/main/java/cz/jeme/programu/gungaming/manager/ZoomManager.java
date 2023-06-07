package cz.jeme.programu.gungaming.manager;

import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.Packets;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.world.entity.player.Abilities;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZoomManager {

    private final Map<Player, ItemStack> helmetItems = new HashMap<>();

    private final ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);

    public ZoomManager() {
        pumpkin.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
        pumpkin.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
        ItemMeta meta = pumpkin.getItemMeta();
        List<Component> lore = new ArrayList<>();
        assert meta != null;
        meta.lore(lore);
        meta.displayName(Messages.from("§"));
        // This is equivalent to an empty char, minecraft cannot render paragraphs, they are used for colors
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setCustomModelData(1);
        pumpkin.setItemMeta(meta);
    }

    public void nextZoom(Player player, double multiplier) {
        if (!helmetItems.containsKey(player) || helmetItems.get(player).equals(pumpkin)) {
            zoomIn(player, multiplier);
            return;
        }
        zoomOut(player);
    }

    public void zoomIn(Player player, double multiplier) {
        if (helmetItems.containsKey(player) && !helmetItems.get(player).equals(pumpkin)) {
            return;
        }
        if (player.isFlying()) {
            player.sendActionBar(Messages.from("<red>You can't use scope while flying!</red>"));
            return;
        }
        setZoom(player, multiplier);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 255, true, false, false));
        showScope(player);
    }

    public void zoomOut(Player player) {
        if (!helmetItems.containsKey(player)) {
            return;
        }
        if (helmetItems.get(player).equals(pumpkin)) {
            return;
        }
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        setZoom(player, 1);
        hideScope(player);
    }

    public void setZoom(Player player, double multiplier) {
        Abilities abilities = new Abilities();
        abilities.walkingSpeed = calcZoom(multiplier);
        // Modifying walking speed in ability NMS packet causes bad behavior to flying!

        Packets.sendPacket(player, new ClientboundPlayerAbilitiesPacket(abilities));
        player.sendActionBar(Messages.from("<gold>zoom: </gold><aqua>" + multiplier + "×</aqua>"));
    }

    private float calcZoom(double multiplier) {
        if (multiplier < 1 || multiplier > 10) {
            throw new IllegalArgumentException("1.0 - 10.0 expected, got " + multiplier);
        }
        return (float) (1.0 / (20 / multiplier - 10));
    }

    private void showScope(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack helmet = inventory.getHelmet();
        if (helmet == null) {
            helmet = new ItemStack(Material.AIR);
        }
        helmetItems.put(player, helmet);
        inventory.setHelmet(pumpkin);
    }

    private void hideScope(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.setHelmet(helmetItems.get(player));
        helmetItems.put(player, pumpkin);
    }

    public void zoomOutAll() {
        for (Player player : helmetItems.keySet()) {
            zoomOut(player);
        }
    }
}