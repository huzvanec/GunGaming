package cz.jeme.programu.gungaming.item.throwable.impl;

import cz.jeme.programu.gungaming.item.throwable.Throwable;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

public class MolotovCocktail extends Throwable {
    @Override
    protected int provideThrowCooldown() {
        return 10;
    }

    @Override
    protected double provideMaxDamage() {
        return 2;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Molotov Cocktail");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "spreads fire everywhere";
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
        return 4;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "molotov_cocktail";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 4;
    }

    @Override
    protected void onThrownHit(final @NotNull ProjectileHitEvent event, final @NotNull Snowball thrown) {
        thrown.getLocation().createExplosion(thrown, 5.5F, true, false);
    }
}
