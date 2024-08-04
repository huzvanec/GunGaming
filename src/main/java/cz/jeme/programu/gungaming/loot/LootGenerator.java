package cz.jeme.programu.gungaming.loot;

import cz.jeme.programu.gungaming.ElementManager;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.loot.crate.Crate;
import cz.jeme.programu.gungaming.loot.crate.CrateFilter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public enum LootGenerator {
    INSTANCE;

    private final @NotNull Random random = new Random();
    private final @NotNull Set<CustomItem> loot = ElementManager.INSTANCE.elements().stream()
            .filter(CustomItem.class::isInstance)
            .map(CustomItem.class::cast)
            .filter(item -> item.rarity() != Rarity.UNOBTAINABLE)
            .collect(Collectors.toSet());

    public ItemStack @NotNull [] generate(final @NotNull Crate crate, final int size) {
        final List<CustomItem> lootPool = new ArrayList<>();
        final CrateFilter filter = crate.filter();
        for (final CustomItem customItem : loot) {
            if (!filter.check(customItem)) continue;
            final int chance = crate.rarityChances().get(customItem.rarity());
            if (chance == 0) continue;
            for (int i = 0; i < chance; i++) lootPool.add(customItem);
        }
        final List<CustomItem> items = new ArrayList<>();
        int i = 0;
        while (i < size) {
            final CustomItem item = random(crate, lootPool);
            if (!checkLimits(crate, items, item)) continue;
            items.add(item);
            i++;
        }

        return items.stream()
                .map(customItem -> {
                    if (customItem == null) return null;
                    final ItemStack item = customItem.item();
                    item.setAmount(randomAmount(customItem));
                    return item;
                })
                .toArray(ItemStack[]::new);
    }


    private @Nullable CustomItem random(final @NotNull Crate crate, final @NotNull List<CustomItem> lootPool) {
        if (random.nextDouble() > crate.fillPercentage() || lootPool.isEmpty()) return null;
        return lootPool.size() == 1
                ? lootPool.getFirst()
                : lootPool.get(random.nextInt(lootPool.size() - 1));
    }

    private static boolean checkLimits(final @NotNull Crate crate, final @NotNull List<CustomItem> items, final @Nullable CustomItem item) {
        if (item == null) return true;
        if (item instanceof SingleLoot && items.contains(item)) return false;
        final Map.Entry<Class<? extends CustomItem>, Integer> limitEntry = crate.limits().entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(item.getClass()))
                .min(Map.Entry.comparingByValue())
                .orElse(null);
        if (limitEntry == null) return true;
        final Class<? extends CustomItem> limitClass = limitEntry.getKey();
        final int limit = limitEntry.getValue();
        final long occurrences = items.stream()
                .filter(limitClass::isInstance)
                .count();
        return occurrences < limit;
    }

    private int randomAmount(@NotNull final CustomItem customItem) {
        final int min = customItem.minAmount();
        final int max = customItem.maxAmount();

        if (min == max) return min;
        return random.nextInt((max - min) + 1) + min;
    }
}