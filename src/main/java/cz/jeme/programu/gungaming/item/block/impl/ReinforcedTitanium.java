package cz.jeme.programu.gungaming.item.block.impl;

import cz.jeme.programu.gungaming.item.block.CustomBlock;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class ReinforcedTitanium extends CustomBlock {
    @Override
    protected @NotNull Component provideName() {
        return Component.text("Reinforced Titanium");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Hard material, resists explosions";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.REINFORCED_DEEPSLATE;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected int provideMinAmount() {
        return 5;
    }

    @Override
    protected int provideMaxAmount() {
        return 15;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "reinforced_titanium";
    }
}
