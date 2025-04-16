package cz.jeme.gungaming.item.throwable.impl;

import cz.jeme.gungaming.item.gun.impl.RocketLauncher;
import cz.jeme.gungaming.item.throwable.MineChainTrigger;
import cz.jeme.gungaming.item.throwable.Throwable;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class RocketThrowable extends Throwable implements MineChainTrigger {
    @Override
    protected int provideThrowCooldown() {
        return 40;
    }

    @Override
    protected double provideMaxDamage() {
        return RocketLauncher.MAX_DAMAGE;
    }

    @Override
    protected Component provideName() {
        return Component.text("Rocket Throwable");
    }

    @Override
    protected String provideDescription() {
        return "Throwable used for the Rocket Launcher explosion";
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
        return "rocket_throwable";
    }

    @Override
    protected void onThrownHit(final ProjectileHitEvent event, final Snowball thrown) {
        thrown.getLocation().createExplosion(thrown, 7F, true, true);
    }

    @Override
    public double triggerRadius() {
        return 8;
    }
}
