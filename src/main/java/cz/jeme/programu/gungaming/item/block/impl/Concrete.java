package cz.jeme.programu.gungaming.item.block.impl;

import cz.jeme.programu.gungaming.item.block.CustomBlock;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class Concrete extends CustomBlock {
    @Override
    protected @NotNull Component provideName() {
        return Component.text("Concrete");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Basic building block";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.GRAY_CONCRETE;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.COMMON;
    }

    @Override
    protected int provideMinAmount() {
        return 10;
    }

    @Override
    protected int provideMaxAmount() {
        return 20;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "concrete";
    }
}
