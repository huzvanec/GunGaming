package cz.jeme.gungaming.item.consumable.impl;

import cz.jeme.gungaming.item.consumable.InstantHeal;
import cz.jeme.gungaming.loot.Rarity;
import io.papermc.paper.datacomponent.item.Consumable;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Medkit extends InstantHeal {
    protected Medkit() {
        item.editMeta(meta -> meta.setMaxStackSize(1));
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    protected void buildConsumable(final Consumable.Builder builder) {
        builder.consumeSeconds(5);
    }

    @Override
    protected double provideHealAmount() {
        return 999_999_999;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "medkit";
    }

    @Override
    protected Component provideName() {
        return Component.text("Medkit");
    }

    @Override
    protected String provideDescription() {
        return "Instantly heals you to full health";
    }

    @Override
    protected Rarity provideRarity() {
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
