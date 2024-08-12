package cz.jeme.programu.gungaming.item.ammo.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class NineMillimeter extends Ammo {
    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "9mm";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("9mm");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Ammo for basic weapons";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.COMMON;
    }

    @Override
    protected int provideMinAmount() {
        return 6;
    }

    @Override
    protected int provideMaxAmount() {
        return 24;
    }
}
