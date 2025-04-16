package cz.jeme.gungaming.item.ammo.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class NineMillimeter extends Ammo {
    @Override
    protected @KeyPattern.Value String provideKey() {
        return "9mm";
    }

    @Override
    protected Component provideName() {
        return Component.text("9mm");
    }

    @Override
    protected String provideDescription() {
        return "Ammo for basic weapons";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.COMMON;
    }

    @Override
    protected int provideMinAmount() {
        return 9;
    }

    @Override
    protected int provideMaxAmount() {
        return 30;
    }
}
