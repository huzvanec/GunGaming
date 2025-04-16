package cz.jeme.gungaming.item.attachment.impl;

import cz.jeme.gungaming.item.attachment.Magazine;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class SmallMagazine extends Magazine {
    @Override
    protected double provideMaxAmmoMultiplier() {
        return 1.1;
    }

    @Override
    protected List<String> provideBuffs() {
        return List.of(
                "+10% ammo capacity"
        );
    }

    @Override
    protected List<String> provideDebuffs() {
        return List.of(
                "-10% reload speed"
        );
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "small_magazine";
    }

    @Override
    protected Component provideName() {
        return Component.text("Small Magazine");
    }

    @Override
    protected String provideDescription() {
        return "Small extended storage for ammo";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.RARE;
    }
}
