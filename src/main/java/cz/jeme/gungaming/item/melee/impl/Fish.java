package cz.jeme.gungaming.item.melee.impl;

import cz.jeme.gungaming.item.melee.Melee;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Fish extends Melee {
    @Override
    protected Component provideName() {
        return Component.text("Fish");
    }

    @Override
    protected String provideDescription() {
        return "Knocks enemies off high cliffs, DO NOT EAT!";
    }

    @Override
    protected Material provideMaterial() {
        return Material.CHORUS_FRUIT;
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "fish";
    }

    @Override
    protected double provideKnockbackBonus() {
        return 1500;
    }
}
