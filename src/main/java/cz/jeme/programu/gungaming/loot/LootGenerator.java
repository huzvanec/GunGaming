package cz.jeme.programu.gungaming.loot;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.item.misc.Misc;
import cz.jeme.programu.gungaming.util.item.Ammos;
import cz.jeme.programu.gungaming.util.item.Attachments;
import cz.jeme.programu.gungaming.util.item.Guns;
import cz.jeme.programu.gungaming.util.item.Miscs;

import java.util.*;

public class LootGenerator {
    public static final List<Loot> LOOT = new ArrayList<>();
    private static final Random RANDOM = new Random();


    private LootGenerator() {
        // Only static generator
    }


    private static void registerLootUnit(CustomItem customItem) {
        for (int i = 0; i < customItem.rarity.chance; i++) {
            Loot loot = new Loot(customItem, customItem instanceof SingleLoot);
            LOOT.add(loot);
        }
    }

    public static void registerLoot() {
        // Register gun loot
        for (Gun gun : Guns.guns.values()) {
            registerLootUnit(gun);
        }

        // Register ammo loot
        for (Ammo ammo : Ammos.ammos.values()) {
            registerLootUnit(ammo);
        }

        // Register miscs
        for (Misc misc : Miscs.miscs.values()) {
            registerLootUnit(misc);
        }

        // Register attachments
        for (Attachment attachment : Attachments.attachments.values()) {
            registerLootUnit(attachment);
        }
    }

    public static Collection<Loot> generate(int slots, int percentage) {
        HashMap<Loot, Class<? extends CustomItem>> items = new HashMap<>();

        for (int i = 0; i < slots; i++) {
            if (RANDOM.nextInt(100) + 1 > percentage) continue;
            Loot lootUnit = LOOT.get(RANDOM.nextInt(LOOT.size()));
            CustomItem customItem = lootUnit.customItem;

            if (customItem instanceof SingleLoot) {
                if (items.containsValue(customItem.group)) continue;
            }

            items.put(lootUnit, customItem.group);
        }
        return items.keySet();
    }
}
