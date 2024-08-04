package cz.jeme.programu.gungaming.loot.crate.impl;

import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.loot.crate.Crate;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AirDrop extends Crate {
    @Override
    protected @NotNull Map<Rarity, Integer> provideRarityChances() {
        return Map.of(
                Rarity.EPIC, 1,
                Rarity.LEGENDARY, 2
        );
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.CHAIN_COMMAND_BLOCK;
    }

    @Override
    protected double provideFillPercentage() {
        return .8;
    }

    @Override
    protected double provideSpawnPercentage() {
        return 0;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "air_drop";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Air Drop");
    }
}
