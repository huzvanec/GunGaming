package cz.jeme.programu.gungaming.item.ammo.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class FiveZeroActionExpress extends Ammo {
    @Override
    protected @NotNull String provideDescription() {
        return "Ammo for the Desert Eagle";
    }

    @Override
    protected int provideMinAmount() {
        return 3;
    }

    @Override
    protected int provideMaxAmount() {
        return 7;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return ".50_action_express";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text(".50 Action Express");
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 6;
    }
}
