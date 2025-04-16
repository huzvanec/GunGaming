package cz.jeme.gungaming.loot.crate.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.loot.Rarity;
import cz.jeme.gungaming.loot.crate.Crate;
import cz.jeme.gungaming.loot.crate.CrateFilter;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.CommandBlock;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

@NullMarked
public class AmmoCrate extends Crate {
    @Override
    protected Map<Rarity, Integer> provideRarityChances() {
        return Map.of(
                Rarity.COMMON, 22,
                Rarity.UNCOMMON, 19,
                Rarity.RARE, 16,
                Rarity.EPIC, 7,
                Rarity.LEGENDARY, 1
        );
    }

    @Override
    protected Material provideMaterial() {
        return Material.COMMAND_BLOCK;
    }

    @Override
    protected double provideFillPercentage() {
        return .3;
    }

    @Override
    protected double provideSpawnPercentage() {
        return .0003;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "ammo_crate";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected Component provideName() {
        return Component.text("Ammo Crate");
    }

    @Override
    protected void generated(final Block block, final Inventory inventory) {
        final CommandBlock data = (CommandBlock) block.getBlockData();
        data.setConditional(true);
        block.setBlockData(data);
    }

    @Override
    protected CrateFilter provideFilter() {
        return new CrateFilter(CrateFilter.CrateFilterType.WHITELIST)
                .add(Ammo.class);
    }
}
