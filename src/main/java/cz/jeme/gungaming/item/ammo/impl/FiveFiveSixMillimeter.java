package cz.jeme.gungaming.item.ammo.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class FiveFiveSixMillimeter extends Ammo {
    @Override
    protected String provideDescription() {
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
    protected @KeyPattern.Value String provideKey() {
        return "5.56mm";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    protected Component provideName() {
        return Component.text("5.56mm");
    }
}
