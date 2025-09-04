package cz.jeme.gungaming.loot;

import cz.jeme.gungaming.ElementManager;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.loot.crate.Crate;
import cz.jeme.gungaming.loot.crate.CrateFilter;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@NullMarked
public enum LootGenerator {
    INSTANCE;

    private final Random random = ThreadLocalRandom.current();
    private final Set<CustomItem> loot = ElementManager.INSTANCE.items().stream()
            .filter(item -> item.rarity() != Rarity.UNOBTAINABLE)
            .collect(Collectors.toSet());

    public @Nullable ItemStack[] generate(final Crate crate, final int size) {
        final List<CustomItem> lootPool = new ArrayList<>();
        final CrateFilter filter = crate.filter();
        for (final CustomItem customItem : loot) {
            if (!filter.check(customItem)) continue;
            final int chance = crate.rarityChances().get(customItem.rarity());
            if (chance == 0) continue;
            for (int i = 0; i < chance; i++) lootPool.add(customItem);
        }
        final List<@Nullable CustomItem> items = new ArrayList<>();
        int i = 0;
        while (i < size) {
            if (random.nextDouble() > crate.fillPercentage() || lootPool.isEmpty()) {
                items.add(null);
            } else {
                final CustomItem item = random(crate, lootPool);
                if (!checkLimits(crate, items, item)) continue;
                items.add(item);
            }
            i++;
        }

        return items.stream()
                .map(customItem -> {
                    if (customItem == null) return null;
                    final ItemStack item = customItem.createStack();
                    item.setAmount(randomAmount(customItem));
                    customItem.generated(item, crate);
                    return item;
                })
                .toArray(ItemStack[]::new);
    }


    private CustomItem random(final Crate crate, final List<CustomItem> lootPool) {
        if (lootPool.isEmpty())
            throw new IllegalArgumentException("Loot pool is empty!");
        return lootPool.size() == 1
                ? lootPool.getFirst()
                : lootPool.get(random.nextInt(lootPool.size() - 1));
    }

    private static boolean checkLimits(final Crate crate, final List<@Nullable CustomItem> items, final CustomItem item) {
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

    private int randomAmount(final CustomItem customItem) {
        final int min = customItem.minAmount();
        final int max = customItem.maxAmount();

        if (min == max) return min;
        return random.nextInt(min, max + 1);
    }
}
