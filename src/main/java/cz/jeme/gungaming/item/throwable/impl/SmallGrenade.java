package cz.jeme.gungaming.item.throwable.impl;

import cz.jeme.gungaming.item.throwable.Grenade;
import cz.jeme.gungaming.item.throwable.MineChainTrigger;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class SmallGrenade extends Grenade implements MineChainTrigger {
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
    protected Component provideName() {
        return Component.text("Small Grenade");
    }

    @Override
    protected String provideDescription() {
        return "Grenade used for the small MIRV explosion";
    }

    @Override
    protected Rarity provideRarity() {
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
    protected @KeyPattern.Value String provideKey() {
        return "small_grenade";
    }

    @Override
    protected void onThrownHit(final ProjectileHitEvent event, final Snowball thrown) {
        thrown.getLocation().createExplosion(thrown, 2.2F, false, true);
    }

    @Override
    public double triggerRadius() {
        return 3;
    }
}
