package cz.jeme.gungaming.item.ammo.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class SevenSixTwoMillimeter extends Ammo {
    @Override
    protected Component provideName() {
        return Component.text("7.62mm");
    }

    @Override
    protected String provideDescription() {
        return "Ammo for better weapons";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.COMMON;
    }

    @Override
    protected int provideMinAmount() {
        return 8;
    }

    @Override
    protected int provideMaxAmount() {
        return 24;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "7.62mm";
    }

}
