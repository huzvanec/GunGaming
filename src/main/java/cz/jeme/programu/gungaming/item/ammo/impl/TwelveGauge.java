package cz.jeme.programu.gungaming.item.ammo.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class TwelveGauge extends Ammo {
    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "12_gauge";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("12 Gauge");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Ammo for shotguns";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 4;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    protected int provideMinAmount() {
        return 4;
    }

    @Override
    protected int provideMaxAmount() {
        return 10;
    }
}
