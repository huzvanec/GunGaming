package cz.jeme.programu.gungaming.item.ammo.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class FiveFiveSixMillimeter extends Ammo {
    @Override
    protected @NotNull String provideDescription() {
        return "Ammo for great and reliable weapons";
    }

    @Override
    protected int provideMinAmount() {
        return 6;
    }

    @Override
    protected int provideMaxAmount() {
        return 22;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "5.56mm";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("5.56mm");
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 7;
    }
}
