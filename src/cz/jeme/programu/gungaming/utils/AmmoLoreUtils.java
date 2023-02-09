package cz.jeme.programu.gungaming.utils;

import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class AmmoLoreUtils {

	private AmmoLoreUtils() {
		// Only static utils
	}

	public static void setAmmo(ItemStack item, int ammo) {
		if (!GunUtils.isGun(item)) {
			throw new IllegalArgumentException("Not a gun!");
		}
		if (ammo < 0) {
			throw new IllegalArgumentException("Negative ammo!");
		}

		int maxAmmo = getMaxAmmo(item);

		if (ammo > maxAmmo) {
			throw new IllegalArgumentException("Ammo higher than max ammo!");
		}

		LoreUtils.updateLore(item, "Ammo", String.valueOf(ammo) + "/" + String.valueOf(maxAmmo));

		Damageable damagableHeldMeta = (Damageable) item.getItemMeta();
		int maxDamage = item.getType().getMaxDurability();
		int damage;
		if (ammo == 0) {
			damage = maxDamage;
		} else {
			damage = Math.round(maxDamage - maxDamage / (maxAmmo / (float) ammo));
		}
		damagableHeldMeta.setDamage(damage);
		item.setItemMeta(damagableHeldMeta);
	}

	public static void addAmmo(ItemStack item, int amount) {
		setAmmo(item, getAmmo(item) + amount);
	}

	public static void removeAmmo(ItemStack item, int amount) {
		addAmmo(item, amount * -1);
	}

	public static int getAmmo(ItemStack item) {
		if (!hasAmmo(item)) {
			return -1;
		}
		Map<String, String> lore = LoreUtils.getLore(item);
		String ammoLore = lore.get("Ammo");
		String ammo = ammoLore.split("/")[0];
		return Integer.valueOf(ammo);
	}

	public static int getMaxAmmo(ItemStack item) {
		if (!hasAmmo(item)) {
			return -1;
		}
		Map<String, String> lore = LoreUtils.getLore(item);
		String ammoLore = lore.get("Ammo");
		String maxAmmo = ammoLore.split("/")[1];
		return Integer.valueOf(maxAmmo);
	}

	public static boolean hasAmmo(ItemStack item) {
		return LoreUtils.getLore(item).containsKey("Ammo");
	}

	public static boolean isAtMaxAmmo(ItemStack item) {
		if (!hasAmmo(item)) {
			return false;
		}
		return getAmmo(item) == getMaxAmmo(item);
	}
}
