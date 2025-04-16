package cz.jeme.gungaming.item.throwable.impl;

import cz.jeme.gungaming.item.throwable.MineChainTrigger;
import cz.jeme.gungaming.item.throwable.Throwable;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MolotovCocktail extends Throwable implements MineChainTrigger {
    @Override
    protected int provideThrowCooldown() {
        return 10;
    }

    @Override
    protected double provideMaxDamage() {
        return 2;
    }

    @Override
    protected Component provideName() {
        return Component.text("Molotov Cocktail");
    }

    @Override
    protected String provideDescription() {
        return "spreads fire everywhere";
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

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "molotov_cocktail";
    }

    @Override
    protected void onThrownHit(final ProjectileHitEvent event, final Snowball thrown) {
        thrown.getLocation().createExplosion(thrown, 5.5F, true, false);
    }

    @Override
    public double triggerRadius() {
        return 6;
    }
}
