package cz.jeme.programu.gungaming.item.ammo.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class SevenSixTwoMillimeter extends Ammo {
    @Override
    protected @NotNull Component provideName() {
        return Component.text("7.62mm");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Ammo for better weapons";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.COMMON;
    }

    @Override
    protected int provideMinAmount() {
        return 3;
    }

    @Override
    protected int provideMaxAmount() {
        return 12;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "7.62mm";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 2;
    }
}
