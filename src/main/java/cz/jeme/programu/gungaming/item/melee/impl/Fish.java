package cz.jeme.programu.gungaming.item.melee.impl;

import cz.jeme.programu.gungaming.item.melee.Melee;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Fish extends Melee {
    @Override
    protected double provideDamage() {
        return 1;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Fish");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "may or may not have knockback 1500";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.CHORUS_FRUIT;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "fish";
    }

    @Override
    protected double provideKnockback() {
        return 1500;
    }

    @Override
    protected double provideAttackSpeed() {
        return 0;
    }

    @Override
    protected @Nullable Integer provideCustomModelData() {
        return 5;
    }
}
