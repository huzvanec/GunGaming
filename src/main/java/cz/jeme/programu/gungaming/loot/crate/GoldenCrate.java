package cz.jeme.programu.gungaming.loot.crate;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public final class GoldenCrate extends Crate {
    @Override
    public @NotNull String getName() {
        return "Golden Crate";
    }

    @Override
    protected @NotNull Map<Rarity, Integer> getChanceOverrides() {
        return Map.of(
                Rarity.COMMON, 5,
                Rarity.UNCOMMON, 5,
                Rarity.RARE, 5,
                Rarity.EPIC, 9,
                Rarity.LEGENDARY, 8
        );
    }

    @Override
    public @NotNull Map<Class<? extends CustomItem>, Integer> getLimits() {
        return Map.of(
                Gun.class, 2,
                Attachment.class, 2
        );
    }

    @Override
    public @NotNull Set<Class<? extends CustomItem>> getFilter() {
        return Collections.emptySet();
    }

    @Override
    public @NotNull Crate.FilterType getFilterType() {
        return FilterType.BLACKLIST;
    }

    @Override
    public float getFillPercentage() {
        return 0.5f;
    }

    @Override
    public float getSpawnPercentage() {
        return 0.0002f;
    }

    @Override
    public @NotNull Material getBlock() {
        return Material.REPEATING_COMMAND_BLOCK;
    }

    @Override
    public @NotNull Consumer<Block> getBlockAction() {
        return getConditionalConsumer(true);
    }
}
