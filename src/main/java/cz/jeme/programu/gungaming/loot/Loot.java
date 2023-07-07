package cz.jeme.programu.gungaming.loot;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.util.item.Ammos;
import cz.jeme.programu.gungaming.util.item.Attachments;
import cz.jeme.programu.gungaming.util.item.Guns;
import cz.jeme.programu.gungaming.util.item.Miscs;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Loot {
    private static Map<CustomItem, Rarity> loot = new HashMap<>();
    private static final Random RANDOM = new Random();

    public static void registerLoot() {
        registerUnit(Ammos.ammos);
        registerUnit(Attachments.attachments);
        registerUnit(Guns.guns);
        registerUnit(Miscs.miscs);

        loot = Collections.unmodifiableMap(loot);
    }

    private static void registerUnit(Map<String, ? extends CustomItem> customItems) {
        for (CustomItem customItem : customItems.values()) {
            loot.put(customItem, customItem.rarity);
        }
    }

    public static List<ItemStack> generate(int slots, Crate crate) {
        List<ItemStack> items = new ArrayList<>();
        List<CustomItem> crateLoot = new ArrayList<>();
        Map<Class<? extends CustomItem>, Integer> limits = new HashMap<>(crate.limits);

        for (CustomItem customItem : loot.keySet()) {
            Rarity rarity = loot.get(customItem);
            for (int i = 0; i < crate.getChance(rarity); i++) {
                crateLoot.add(customItem);
            }
        }
        int limited = 0;
        generate:
        for (int slot = 0; slot < slots + limited; slot++) {
            if (RANDOM.nextFloat() * 100 > crate.percentage) {
                items.add(null);
                continue;
            }
            CustomItem customItem = crateLoot.get(RANDOM.nextInt(crateLoot.size()));
            for (Class<? extends CustomItem> clazz : limits.keySet()) {
                if (clazz.isInstance(customItem)) {
                    if (limits.get(clazz) == 0) {
                        limited++;
                        continue generate;
                    }
                    limits.put(clazz, limits.get(clazz) - 1);
                    break;
                }
            }
            ItemStack item = new ItemStack(customItem.item);
            item.setAmount(getAmount(customItem));
            items.add(item);
            if (customItem instanceof SingleLoot) {
                crateLoot.removeIf(ci -> ci == customItem);
            }
        }
        return items;
    }

    private static int getAmount(CustomItem customItem) {
        final int min = customItem.getMinLoot();
        final int max = customItem.getMaxLoot();

        if (min == max) return min;
        return RANDOM.nextInt((max - min) + 1) + min;
    }
}
