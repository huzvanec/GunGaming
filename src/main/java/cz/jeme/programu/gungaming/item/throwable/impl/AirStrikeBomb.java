package cz.jeme.programu.gungaming.item.throwable.impl;

import cz.jeme.programu.gungaming.item.throwable.MineChainTrigger;
import cz.jeme.programu.gungaming.item.throwable.Throwable;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

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
    protected @NotNull String provideDescription() {
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
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "air_strike_bomb";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.UNOBTAINABLE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Air Strike Bomb");
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 8;
    }

    @Override
    protected void onThrownHit(final @NotNull ProjectileHitEvent event, final @NotNull Snowball thrown) {
        thrown.getLocation().createExplosion(thrown, 5F, false, true);
    }

    @Override
    public double triggerRadius() {
        return 6;
    }
}
