package cz.jeme.gungaming.item.ammo.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ThreeZeroEightSubsonicWinchester extends Ammo {
    @Override
    protected String provideDescription() {
        return "Ammo for the best sniper rifles";
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
    protected @KeyPattern.Value String provideKey() {
        return ".308_subsonic_winchester";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected Component provideName() {
        return Component.text(".308 Subsonic Winchester");
    }

}
