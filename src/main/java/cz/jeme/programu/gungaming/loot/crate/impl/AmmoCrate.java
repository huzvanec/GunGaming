package cz.jeme.programu.gungaming.loot.crate.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.loot.crate.Crate;
import cz.jeme.programu.gungaming.loot.crate.CrateFilter;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.CommandBlock;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AmmoCrate extends Crate {
    @Override
    protected @NotNull Map<Rarity, Integer> provideRarityChances() {
        return Map.of(
                Rarity.COMMON, 14,
                Rarity.UNCOMMON, 12,
                Rarity.RARE, 8,
                Rarity.EPIC, 3,
                Rarity.LEGENDARY, 1
        );
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.COMMAND_BLOCK;
    }

    @Override
    protected double provideFillPercentage() {
        return .4;
    }

    @Override
    protected double provideSpawnPercentage() {
        return .00012;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "ammo_crate";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Ammo Crate");
    }

    @Override
    protected void generated(final @NotNull Block block, final @NotNull Inventory inventory) {
        final CommandBlock data = (CommandBlock) block.getBlockData();
        data.setConditional(true);
        block.setBlockData(data);
    }

    @Override
    protected @NotNull CrateFilter provideFilter() {
        return new CrateFilter(CrateFilter.CrateFilterType.WHITELIST)
                .add(Ammo.class);
    }
}
