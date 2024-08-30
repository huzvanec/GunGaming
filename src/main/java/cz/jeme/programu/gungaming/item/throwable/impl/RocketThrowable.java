package cz.jeme.programu.gungaming.item.throwable.impl;

import cz.jeme.programu.gungaming.item.gun.impl.RocketLauncher;
import cz.jeme.programu.gungaming.item.throwable.Throwable;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

public class RocketThrowable extends Throwable {
    @Override
    protected int provideThrowCooldown() {
        return 40;
    }

    @Override
    protected double provideMaxDamage() {
        return RocketLauncher.MAX_DAMAGE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Rocket Throwable");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Throwable used for the Rocket Launcher explosion";
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
        return "rocket_throwable";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 9;
    }

    @Override
    protected void onThrownHit(final @NotNull ProjectileHitEvent event, final @NotNull Snowball thrown) {
        thrown.getLocation().createExplosion(thrown, 7F, true, true);
    }
}
