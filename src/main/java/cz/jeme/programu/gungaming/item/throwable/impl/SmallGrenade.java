package cz.jeme.programu.gungaming.item.throwable.impl;

import cz.jeme.programu.gungaming.item.throwable.Grenade;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

public class SmallGrenade extends Grenade {
    public static final double MAX_DAMAGE = 14;

    @Override
    protected int provideThrowCooldown() {
        return 15;
    }

    @Override
    protected double provideMaxDamage() {
        return MAX_DAMAGE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Small Grenade");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Grenade used for the small MIRV explosion";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.UNOBTAINABLE;
    }

    @Override
    protected int provideMinAmount() {
        return 0;
    }

    @Override
    protected int provideMaxAmount() {
        return 0;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "small_grenade";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 5;
    }

    @Override
    protected void onThrownHit(final @NotNull ProjectileHitEvent event, final @NotNull Snowball thrown) {
        thrown.getLocation().createExplosion(thrown, 2.2F, false, true);
    }
}
