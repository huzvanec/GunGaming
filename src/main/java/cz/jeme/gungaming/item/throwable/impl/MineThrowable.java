package cz.jeme.gungaming.item.throwable.impl;

import cz.jeme.gungaming.item.block.impl.Mine;
import cz.jeme.gungaming.item.throwable.MineChainTrigger;
import cz.jeme.gungaming.item.throwable.Throwable;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MineThrowable extends Throwable implements MineChainTrigger {
    @Override
    protected int provideThrowCooldown() {
        return 40;
    }

    @Override
    protected double provideMaxDamage() {
        return Mine.MAX_DAMAGE;
    }

    @Override
    protected Component provideName() {
        return Component.text("Mine Throwable");
    }

    @Override
    protected String provideDescription() {
        return "Throwable used for the Mine explosion";
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
        return "mine_throwable";
    }

    @Override
    protected void onThrownHit(final ProjectileHitEvent event, final Snowball thrown) {
        thrown.getLocation().createExplosion(thrown, 6, true, true);
    }

    @Override
    public double triggerRadius() {
        return 7;
    }
}
