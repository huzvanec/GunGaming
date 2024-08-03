package cz.jeme.programu.gungaming.item.consumable.impl;

import cz.jeme.programu.gungaming.item.consumable.InstantHeal;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class Bandage extends InstantHeal {
    @Override
    protected double provideHealAmount() {
        return 5;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "bandage";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Bandage");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Instantly heals 2.5 hearts";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 5;
    }
}
