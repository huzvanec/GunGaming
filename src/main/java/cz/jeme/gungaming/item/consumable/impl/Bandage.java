package cz.jeme.gungaming.item.consumable.impl;

import cz.jeme.gungaming.item.consumable.InstantHeal;
import cz.jeme.gungaming.loot.Rarity;
import io.papermc.paper.datacomponent.item.Consumable;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Bandage extends InstantHeal {
    @Override
    protected double provideHealAmount() {
        return 4;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "bandage";
    }

    @Override
    protected Component provideName() {
        return Component.text("Bandage");
    }

    @Override
    protected String provideDescription() {
        return "Instantly heals 2 hearts";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 3;
    }
}
