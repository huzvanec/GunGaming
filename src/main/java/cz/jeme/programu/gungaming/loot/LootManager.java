package cz.jeme.programu.gungaming.loot;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.loot.crate.Crate;
import cz.jeme.programu.gungaming.util.registry.Groups;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public enum LootManager {
    INSTANCE;
    private @NotNull Map<CustomItem, Rarity> loot = new HashMap<>();
    private static final @NotNull Random RANDOM = new Random();

    public final void registerGroups() {
        Groups.groups.values().forEach(m -> m.values().forEach(ci -> loot.put(ci, ci.rarity)));
        loot = Collections.unmodifiableMap(loot);
    }

    public @NotNull List<ItemStack> generateLoot(@NotNull Crate crate, int slots) {
        List<ItemStack> items = new ArrayList<>();
        List<CustomItem> crateLoot = new ArrayList<>();
        Map<Class<? extends CustomItem>, Integer> limits = new HashMap<>(crate.getLimits());

        for (Map.Entry<? extends CustomItem, Rarity> entry : loot.entrySet()) {
            CustomItem customItem = entry.getKey();
            Rarity rarity = entry.getValue();

            for (int i = 0; i < crate.getChances().get(rarity); i++) {
                if (filterCheck(crate, customItem)) crateLoot.add(customItem);
            }
        }

        int limited = 0;

        generation:
        for (int slot = 0; slot < slots + limited; slot++) {
            if (RANDOM.nextFloat() > crate.getFillPercentage()) {
                items.add(null); // Empty item slot
                continue;
            }

            final CustomItem customItem = crateLoot.get(RANDOM.nextInt(crateLoot.size()));

            for (Map.Entry<Class<? extends CustomItem>, Integer> entry : limits.entrySet()) {
                Class<? extends CustomItem> clazz = entry.getKey();
                Integer limit = entry.getValue();

                if (clazz.isInstance(customItem)) {
                    if (limit == 0) {
                        limited++;
                        continue generation;
                    }
                    limits.put(clazz, limit - 1);
                    break;
                }
            }

            ItemStack item = new ItemStack(customItem.item);
            item.setAmount(getRandomAmount(customItem));
            items.add(item);
            if (customItem instanceof SingletonLoot) {
                crateLoot.removeIf(ci -> ci == customItem);
            }
        }
        return items;
    }

    private boolean filterCheck(@NotNull Crate crate, @NotNull CustomItem customItem) {
        boolean contains = crate.getFilter().stream().anyMatch(c -> c.isInstance(customItem));
        return switch (crate.getFilterType()) {
            case WHITELIST -> contains;
            case BLACKLIST -> !contains;
        };
    }

    private static int getRandomAmount(@NotNull CustomItem customItem) {
        final int min = customItem.getMinLoot();
        final int max = customItem.getMaxLoot();

        if (min == max) return min;
        return RANDOM.nextInt((max - min) + 1) + min;
    }
}
