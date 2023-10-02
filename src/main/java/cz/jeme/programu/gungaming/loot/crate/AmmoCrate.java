package cz.jeme.programu.gungaming.loot.crate;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public final class AmmoCrate extends Crate {
    @Override
    public @NotNull String getName() {
        return "Ammo Crate";
    }

    @Override
    protected @NotNull Map<Rarity, Integer> getChanceOverrides() {
        return Collections.emptyMap();
    }

    @Override
    public @NotNull Map<Class<? extends CustomItem>, Integer> getLimits() {
        return Collections.emptyMap();
    }

    @Override
    public @NotNull Set<Class<? extends CustomItem>> getFilter() {
        return Set.of(Ammo.class);
    }

    @Override
    public @NotNull Crate.FilterType getFilterType() {
        return FilterType.WHITELIST;
    }

    @Override
    public float getFillPercentage() {
        return 0.25f;
    }

    @Override
    public float getSpawnPercentage() {
        return 0.0004f;
    }

    @Override
    public @NotNull Material getBlock() {
        return Material.COMMAND_BLOCK;
    }

    @Override
    public @NotNull Consumer<Block> getBlockAction() {
        return getConditionalConsumer(true);
    }
}
