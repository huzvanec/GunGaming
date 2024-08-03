package cz.jeme.programu.gungaming.item.consumable.impl;

import cz.jeme.programu.gungaming.item.consumable.InstantHeal;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class Medkit extends InstantHeal {
    protected Medkit() {
        item.editMeta(meta -> meta.setMaxStackSize(1));
    }

    @Override
    protected double provideHealAmount() {
        return 999_999_999;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "medkit";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Medkit");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Instantly heals you to full health";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 4;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 1;
    }
}
