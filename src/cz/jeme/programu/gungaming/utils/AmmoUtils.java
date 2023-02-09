package cz.jeme.programu.gungaming.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import cz.jeme.programu.gungaming.items.ammo.Ammo;
import cz.jeme.programu.gungaming.items.guns.Gun;
import net.md_5.bungee.api.ChatColor;

public class AmmoUtils {

	public static Map<String, Ammo> ammos = new HashMap<String, Ammo>();

	private AmmoUtils() {
		// Only static utils
	}

	public static void register(Ammo ammo) {
		ammos.put(ChatColor.stripColor(ammo.name), ammo);
	}

	public static Ammo getAmmo(String name) {
		return ammos.get(name);
	}

	public static Ammo getAmmo(ItemStack item) {
		return getAmmo(GunUtils.getGun(item));
	}

	public static Ammo getAmmo(Gun gun) {
		return getAmmo(gun.ammoTypeName);
	}
}
