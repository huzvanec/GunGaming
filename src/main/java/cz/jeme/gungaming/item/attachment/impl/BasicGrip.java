package cz.jeme.gungaming.item.attachment.impl;

import cz.jeme.gungaming.item.attachment.Grip;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class BasicGrip extends Grip {
    @Override
    protected double provideInaccuracyMultiplier() {
        return 0.7;
    }

    @Override
    protected List<String> provideBuffs() {
        return List.of(
                "+30% accuracy"
        );
    }

    @Override
    protected List<String> provideDebuffs() {
        return List.of();
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "basic_grip";
    }

    @Override
    protected Component provideName() {
        return Component.text("Basic Grip");
    }

    @Override
    protected String provideDescription() {
        return "Good weapon accuracy";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.RARE;
    }
}
