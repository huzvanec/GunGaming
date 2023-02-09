package cz.jeme.programu.gungaming.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
	private InventoryUtils() {
		// Only static utils
	}

	public static int getItemCount(Inventory inventory, ItemStack item) {
		int count = 0;
		for (ItemStack invItem : inventory) {
			if (invItem == null) {
				continue;
			}
			if (invItem.isSimilar(item)) {
				count = count + invItem.getAmount();
			}
		}
		return count;
	}

	public static void removeItems(Inventory inventory, ItemStack item, int count) {
		assert count <= getItemCount(inventory, item) : "Player has less items!";

		for (ItemStack invItem : inventory.getContents()) {
			if (invItem == null) {
				continue;
			}
			if (invItem.isSimilar(item)) {
				int amount = invItem.getAmount();
				if (amount <= count) {
					count = count - amount;
					invItem.setAmount(0);
				} else {
					invItem.setAmount(amount - count);
					count = 0;
				}
				if (count == 0) {
					break;
				}
			}
		}

	}
}
