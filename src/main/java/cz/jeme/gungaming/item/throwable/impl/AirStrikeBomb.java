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
public class AirStrikeBomb extends Throwable implements MineChainTrigger {
    @Override
    protected int provideThrowCooldown() {
        return 100;
    }

    @Override
    protected double provideMaxDamage() {
        return 20;
    }

    @Override
    protected String provideDescription() {
        return "Explosive used for the air strike bombing";
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
        return "air_strike_bomb";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.UNOBTAINABLE;
    }

    @Override
    protected Component provideName() {
        return Component.text("Air Strike Bomb");
    }

    @Override
    protected void onThrownHit(final ProjectileHitEvent event, final Snowball thrown) {
        thrown.getLocation().createExplosion(thrown, 5F, false, true);
    }

    @Override
    public double triggerRadius() {
        return 6;
    }
}
