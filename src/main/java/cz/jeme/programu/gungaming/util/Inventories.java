package cz.jeme.programu.gungaming.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class Inventories {
    private Inventories() {
        // Static class cannot be initialized
    }

    public static int getItemCount(@NotNull Inventory inventory, @NotNull ItemStack searchItem) {
        int count = 0;
        for (ItemStack invItem : inventory) {
            if (invItem == null) {
                continue;
            }
            if (invItem.isSimilar(searchItem)) {
                count = count + invItem.getAmount();
            }
        }
        return count;
    }

    public static void removeItems(@NotNull Inventory inventory, @NotNull ItemStack item, int count) {
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
