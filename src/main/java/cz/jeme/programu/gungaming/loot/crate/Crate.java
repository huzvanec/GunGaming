package cz.jeme.programu.gungaming.loot.crate;

import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.item.CustomItem;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Crate {
    private final @NotNull Map<Rarity, Integer> chances;

    public Crate() {
        assert getFillPercentage() >= 0 && getFillPercentage() <= 1 : "Fill percentage must be between 0 and 1!";
        assert getSpawnPercentage() >= 0 && getSpawnPercentage() <= 1 : "Spawn percentage must be between 0 and 1!";
        assert !getChanceOverrides().containsKey(Rarity.UNOBTAINABLE) : "You cannot override the chance of Rarity.UNOBTAINABLE!";

        Map<Rarity, Integer> tempChances = new HashMap<>();
        for (Rarity rarity : Rarity.values()) {
            if (getChanceOverrides().containsKey(rarity)) {
                tempChances.put(rarity, getChanceOverrides().get(rarity));
            } else {
                tempChances.put(rarity, rarity.getChance());
            }
        }
        chances = Collections.unmodifiableMap(tempChances);
    }

    public final @NotNull Map<Rarity, Integer> getChances() {
        return chances;
    }

    abstract public @NotNull String getName();

    abstract protected @NotNull Map<Rarity, Integer> getChanceOverrides();

    abstract public @NotNull Map<Class<? extends CustomItem>, Integer> getLimits();

    abstract public @NotNull Set<Class<? extends CustomItem>> getFilter();

    abstract public @NotNull Crate.FilterType getFilterType();

    abstract public float getFillPercentage();

    abstract public float getSpawnPercentage();

    abstract public @NotNull Material getBlock();

    public enum FilterType {
        BLACKLIST,
        WHITELIST
    }
}
