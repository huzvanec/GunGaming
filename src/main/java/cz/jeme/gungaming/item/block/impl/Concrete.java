package cz.jeme.gungaming.item.block.impl;

import cz.jeme.gungaming.item.block.CustomBlock;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Concrete extends CustomBlock {
    @Override
    protected Component provideName() {
        return Component.text("Concrete");
    }

    @Override
    protected String provideDescription() {
        return "Basic building block";
    }

    @Override
    protected Material provideMaterial() {
        return Material.GRAY_CONCRETE;
    }

    @Override
    protected Rarity provideRarity() {
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
    protected @KeyPattern.Value String provideKey() {
        return "concrete";
    }
}
