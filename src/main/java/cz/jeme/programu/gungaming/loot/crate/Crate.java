package cz.jeme.programu.gungaming.loot.crate;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Components;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Crate extends CustomElement {
    protected final @NotNull Map<Rarity, Integer> rarityChances;
    protected final @NotNull Map<Class<? extends CustomItem>, Integer> limits = Collections.unmodifiableMap(provideLimits());
    protected final @NotNull CrateFilter filter = provideFilter();
    protected final @NotNull Material material = provideMaterial();
    protected final double fillPercentage = provideFillPercentage();
    protected final double spawnPercentage = provideSpawnPercentage();
    private final boolean refill = provideRefill();

    protected Crate() {
        final Map<Rarity, Integer> tempRarityChances = new HashMap<>(provideRarityChances());
        for (final Rarity rarity : Rarity.values()) {
            final Integer chance = tempRarityChances.get(rarity);
            if (chance == null || chance < 0)
                tempRarityChances.put(rarity, 0);
        }
        tempRarityChances.put(Rarity.UNOBTAINABLE, 0);
        rarityChances = Collections.unmodifiableMap(tempRarityChances);
    }

    // providers

    protected abstract @NotNull Map<Rarity, Integer> provideRarityChances();

    protected @NotNull Map<Class<? extends CustomItem>, Integer> provideLimits() {
        return Map.of();
    }

    protected @NotNull CrateFilter provideFilter() {
        return CrateFilter.empty();
    }

    protected abstract @NotNull Material provideMaterial();

    protected abstract double provideFillPercentage();

    protected abstract double provideSpawnPercentage();

    protected boolean provideRefill() {
        return true;
    }

    // getters

    public final @NotNull Map<Rarity, Integer> rarityChances() {
        return rarityChances;
    }

    public final @NotNull Map<Class<? extends CustomItem>, Integer> limits() {
        return limits;
    }

    public final @NotNull CrateFilter filter() {
        return new CrateFilter(filter);
    }

    public final @NotNull Material material() {
        return material;
    }

    public final double fillPercentage() {
        return fillPercentage;
    }

    public final double spawnPercentage() {
        return spawnPercentage;
    }

    public final boolean refill() {
        return refill;
    }

    protected void generated(final @NotNull Block block, final @NotNull Inventory inventory) {
    }

    @Override
    public @NotNull String toString() {
        return Components.strip(name);
    }
}