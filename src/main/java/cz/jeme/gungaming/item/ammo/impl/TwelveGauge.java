package cz.jeme.gungaming.item.ammo.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TwelveGauge extends Ammo {
    @Override
    protected @KeyPattern.Value String provideKey() {
        return "12_gauge";
    }

    @Override
    protected Component provideName() {
        return Component.text("12 Gauge");
    }

    @Override
    protected String provideDescription() {
        return "Ammo for shotguns";
    }

    @Override
    protected Rarity provideRarity() {
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
