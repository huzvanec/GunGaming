package cz.jeme.gungaming.loot.crate;

import cz.jeme.gungaming.CustomElement;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.loot.Rarity;
import cz.jeme.gungaming.util.Components;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@NullMarked
public abstract class Crate extends CustomElement {
    protected final Map<Rarity, Integer> rarityChances;
    protected final Map<Class<? extends CustomItem>, Integer> limits = Collections.unmodifiableMap(provideLimits());
    protected final CrateFilter filter = provideFilter();
    protected final Material material = provideMaterial();
    protected final double fillPercentage = provideFillPercentage();
    protected final double spawnPercentage = provideSpawnPercentage();
    private final boolean modifyContents = provideModifyContents();

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

    protected abstract Map<Rarity, Integer> provideRarityChances();

    protected Map<Class<? extends CustomItem>, Integer> provideLimits() {
        return Map.of();
    }

    protected CrateFilter provideFilter() {
        return CrateFilter.empty();
    }

    protected abstract Material provideMaterial();

    protected abstract double provideFillPercentage();

    protected abstract double provideSpawnPercentage();

    protected boolean provideModifyContents() {
        return true;
    }

    // getters

    public final Map<Rarity, Integer> rarityChances() {
        return rarityChances;
    }

    public final Map<Class<? extends CustomItem>, Integer> limits() {
        return limits;
    }

    public final CrateFilter filter() {
        return new CrateFilter(filter);
    }

    public final Material material() {
        return material;
    }

    public final double fillPercentage() {
        return fillPercentage;
    }

    public final double spawnPercentage() {
        return spawnPercentage;
    }

    public final boolean modifyContents() {
        return modifyContents;
    }

    protected void generated(final Block block, final Inventory inventory) {
    }

    @Override
    public String toString() {
        return Components.strip(name);
    }
}