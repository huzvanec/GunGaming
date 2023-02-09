package cz.jeme.programu.gungaming.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public final class LoreUtils {

	private static final String SEPARATOR = ": ";

	private LoreUtils() {
		// Only static utils
	}

	public static List<String> mapToStringList(Map<String, String> map) {
		List<String> list = new ArrayList<String>();

		for (String key : map.keySet()) {
			String value = map.get(key);
			list.add(ChatColor.DARK_AQUA + ChatColor.ITALIC.toString() + key + SEPARATOR + value);
		}
		return list;
	}

	public static Map<String, String> stringListToMap(List<String> list) {
		Map<String, String> map = new HashMap<String, String>();

		for (String item : list) {
			item = ChatColor.stripColor(item);
			String[] splitArray = item.split(SEPARATOR);
			if (splitArray.length != 2) {
				continue;
			}
			map.put(splitArray[0], splitArray[1]);
		}
		return map;
	}

	public static void setLore(ItemStack item, List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		String loreLine = meta.getLore().get(0);
		lore.add(0, loreLine);
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public static void setLore(ItemStack item, Map<String, String> map) {
		setLore(item, mapToStringList(map));
	}

	public static void addLore(ItemStack item, List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		List<String> oldLore = meta.getLore();
		oldLore.addAll(lore);
		meta.setLore(oldLore);
		item.setItemMeta(meta);
	}

	public static void addLore(ItemStack item, Map<String, String> lore) {
		addLore(item, mapToStringList(lore));
	}

	public static Map<String, String> getLore(ItemStack item) {
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			return new HashMap<String, String>();
		}
		return stringListToMap(item.getItemMeta().getLore());
	}

	public static void updateLore(ItemStack item, String key, String value) {
		Map<String, String> lore = getLore(item);
		lore.put(key, value);
		setLore(item, lore);
	}

	public static String getLore(ItemStack item, String key) {
		Map<String, String> lore = getLore(item);
		return (lore.get(key));
	}
}
