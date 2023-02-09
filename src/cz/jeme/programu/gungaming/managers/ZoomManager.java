package cz.jeme.programu.gungaming.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import cz.jeme.programu.gungaming.utils.PacketUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.world.entity.player.Abilities;

public class ZoomManager {

	private Map<Player, ItemStack> helmetItems = new HashMap<Player, ItemStack>();

	private ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);

	public ZoomManager() {
		pumpkin.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
		ItemMeta meta = pumpkin.getItemMeta();
		List<String> lore = new ArrayList<String>();
		meta.setLore(lore);
		meta.setDisplayName("§"); // Empty char, minecraft cannot render paragraphs, they are used for colors
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
		setZoom(player, multiplier);
		showScope(player);
	}

	public void zoomOut(Player player) {
		if (!helmetItems.containsKey(player)) {
			return;
		}
		if (helmetItems.get(player).equals(pumpkin)) {
			return;
		}

		setZoom(player, 1);
		hideScope(player);
	}

	public void setZoom(Player player, double multiplier) {
		Abilities abilities = new Abilities();
		abilities.walkingSpeed = calcZoom(multiplier);
		// Modifying walking speed in ability NMS packet causes bad behavior to flying!

		ClientboundPlayerAbilitiesPacket packet = new ClientboundPlayerAbilitiesPacket(abilities);

		PacketUtils.sendPacket(player, packet);
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent
				.fromLegacyText(ChatColor.GOLD + "zᴏᴏᴍ " + ChatColor.WHITE + String.valueOf(multiplier) + "×"));
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
