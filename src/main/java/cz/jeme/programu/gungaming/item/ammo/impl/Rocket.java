package cz.jeme.programu.gungaming.item.ammo.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class Rocket extends Ammo {
    @Override
    protected @NotNull Component provideName() {
        return Component.text("Rocket");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Ammo for the Rocket Launcher";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 2;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "rocket";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 3;
    }
}
