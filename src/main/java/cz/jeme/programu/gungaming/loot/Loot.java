package cz.jeme.programu.gungaming.loot;

import cz.jeme.programu.gungaming.item.CustomItem;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Loot {
    public final ItemStack item;
    public final int min;
    public final int max;
    public final boolean single;
    public final CustomItem customItem;

    Random random = new Random();

    public Loot(CustomItem customItem, boolean single) {
        this.customItem = customItem;
        this.min = customItem.minLoot;
        this.max = customItem.maxLoot;
        assert min <= 64 && min > 0;
        assert max <= 64 && max >= min;
        this.item = new ItemStack(customItem.item);
        this.single = single;
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
