package cz.jeme.programu.gungaming.loot;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.item.misc.Misc;
import cz.jeme.programu.gungaming.util.item.Ammos;
import cz.jeme.programu.gungaming.util.item.Guns;
import cz.jeme.programu.gungaming.util.item.Miscs;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootGenerator {
    public static final List<Loot> loot = new ArrayList<>();
    private static final Random random = new Random();


    private LootGenerator() {
        // Only static generator
    }

    private static void registerLootUnit(CustomItem customItem, Rarity rarity, int min, int max) {
        for (int i = 0; i < rarity.chance; i++) {
            loot.add(new Loot(customItem.item, min, max));
        }
    }

    public static void registerLoot() {
        // Register gun loot
        for (Gun gun : Guns.guns.values()) {
            registerLootUnit(gun, gun.rarity, 1, 1);
        }

        // Register ammo loot
        for (Ammo ammo : Ammos.ammos.values()) {
            registerLootUnit(ammo, ammo.rarity, ammo.minLoot, ammo.maxLoot);
        }

        // Register miscs
        for (Misc misc : Miscs.miscs.values()) {
            registerLootUnit(misc, misc.rarity, misc.minLoot, misc.maxLoot);
        }
    }

    public static List<ItemStack> generate(int slots, int percentage) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < slots; i++) {
            if (random.nextInt(100) + 1 > percentage) continue;
            Loot lootUnit = loot.get(random.nextInt(loot.size()));
            items.add(lootUnit.getLoot());
        }
        return items;
    }
}
