package cz.jeme.programu.gungaming.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class Inventories {
    private Inventories() {
        throw new AssertionError();
    }

    public static int count(final @NotNull Inventory inventory, final @NotNull ItemStack item) {
        int count = 0;
        for (final ItemStack invItem : inventory) {
            if (invItem == null || !invItem.isSimilar(item)) continue;
            count += invItem.getAmount();
        }
        return count;
    }

    public static int remove(final @NotNull Inventory inventory, final @NotNull ItemStack item, final int count) {
        int left = count;
        for (final ItemStack invItem : inventory) {
            if (invItem == null || !invItem.isSimilar(item)) continue;
            final int amount = invItem.getAmount();
            final int remove = Math.min(amount, left);
            invItem.setAmount(amount - remove);
            left -= remove;
            if (left == 0) break;
        }
        return left;
    }
}
