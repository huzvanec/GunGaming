package cz.jeme.programu.gungaming.item.attachment.impl;

import cz.jeme.programu.gungaming.item.attachment.Grip;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasicGrip extends Grip {
    @Override
    protected double provideInaccuracyMultiplier() {
        return 0.7;
    }

    @Override
    protected @NotNull List<String> provideBuffs() {
        return List.of(
                "+30% accuracy"
        );
    }

    @Override
    protected @NotNull List<String> provideDebuffs() {
        return List.of();
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "basic_grip";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Basic Grip");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Good weapon accuracy";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 2;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }
}
