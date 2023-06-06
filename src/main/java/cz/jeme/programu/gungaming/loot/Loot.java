package cz.jeme.programu.gungaming.loot;

import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Loot {
    public ItemStack item;
    public int min;
    public int max;

    Random random = new Random();

    public Loot(ItemStack item, int min, int max) {
        assert min <= 64 && min > 0;
        assert max <= 64 && max >= min;
        this.item = new ItemStack(item);
        this.min = min;
        this.max = max;
    }

    public ItemStack getLoot() {
        if (min == max) {
            item.setAmount(min);
            return item;
        }
        item.setAmount(random.nextInt((max - min) + 1) + min);
        return new ItemStack(item);
    }
}
