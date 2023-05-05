package cz.jeme.programu.gungaming.loot;

import cz.jeme.programu.gungaming.items.ammo.Ammo;
import cz.jeme.programu.gungaming.items.guns.Gun;
import cz.jeme.programu.gungaming.utils.AmmoUtils;
import cz.jeme.programu.gungaming.utils.GunUtils;
import org.bukkit.Material;
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

    private static void registerLootUnit(ItemStack item, Rarity rarity, int min, int max) {
        for (int i = 0; i < rarity.getChance(); i++) {
            loot.add(new Loot(item, min, max));
        }
    }

    private static void registerLootUnit(Material material, Rarity rarity, int min, int max) {
        registerLootUnit(new ItemStack(material), rarity, min, max);
    }

    public static void registerLoot() {
        for (Gun gun : GunUtils.guns.values()) {
            registerLootUnit(gun.item, gun.rarity, 1, 1);
        }
        for (Ammo ammo : AmmoUtils.ammos.values()) {
            registerLootUnit(ammo.item, ammo.rarity, ammo.minLoot, ammo.maxLoot);
        }
        registerLootUnit(Material.OAK_PLANKS, Rarity.COMMON, 10, 64);
        registerLootUnit(Material.DIAMOND, Rarity.EPIC, 3, 5);
        registerLootUnit(Material.REDSTONE, Rarity.UNCOMMON , 5, 20);
        registerLootUnit(Material.DIAMOND_SWORD, Rarity.LEGENDARY, 1, 2);
    }

    public static List<ItemStack> generateCrate(int slots, int percentage) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < slots; i++) {
            if (random.nextInt(100) + 1 > percentage) {
                continue;
            }
            Loot lootUnit = loot.get(random.nextInt(loot.size()));
            items.add(lootUnit.getLoot());
        }
        return items;
    }
}
