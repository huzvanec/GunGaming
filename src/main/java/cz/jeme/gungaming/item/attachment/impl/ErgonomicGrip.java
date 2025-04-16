package cz.jeme.gungaming.item.attachment.impl;

import cz.jeme.gungaming.item.attachment.Grip;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class ErgonomicGrip extends Grip {
    @Override
    protected double provideInaccuracyMultiplier() {
        return 0.1;
    }

    @Override
    protected List<String> provideBuffs() {
        return List.of(
                "+90% accuracy"
        );
    }

    @Override
    protected List<String> provideDebuffs() {
        return List.of();
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "ergonomic_grip";
    }

    @Override
    protected Component provideName() {
        return Component.text("Ergonomic Grip");
    }

    @Override
    protected String provideDescription() {
        return "Awesome weapon accuracy";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }
}
