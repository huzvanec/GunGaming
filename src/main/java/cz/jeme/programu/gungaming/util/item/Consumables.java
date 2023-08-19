package cz.jeme.programu.gungaming.util.item;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.consumable.Consumable;
import cz.jeme.programu.gungaming.util.Lores;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Consumables {

    public static @NotNull Map<String, Consumable> consumables = new HashMap<>();

    private Consumables() {
        // Static class cannot be initialized
    }

    public static boolean isConsumable(@Nullable ItemStack item) {
        return Namespace.CONSUMABLE.has(item);
    }

    public static @Nullable Consumable getConsumable(@NotNull String name) {
        return consumables.get(name);
    }

    public static @NotNull Consumable getConsumable(@NotNull ItemStack item) {
        String name = Namespace.CONSUMABLE.get(item);
        assert name != null : "This item is not a Consumable!";
        Consumable consumable = getConsumable(name);
        assert consumable != null : "This item has a Consumable tag, that doesn't represent any registered Consumable!";
        return consumable;
    }

    public static void register(@NotNull Consumable consumable) {
        consumables.put(consumable.name, consumable);
        Lores.update(consumable.item);
    }

    public static void registered() {
        consumables = Collections.unmodifiableMap(consumables);
    }
}
