package cz.jeme.programu.gungaming.item.throwable.impl;

import cz.jeme.programu.gungaming.item.throwable.Grenade;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

public class FragGrenade extends Grenade {
    @Override
    protected int provideThrowCooldown() {
        return 30;
    }

    @Override
    protected double provideMaxDamage() {
        return 32D;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "frag_grenade";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Frag Grenade");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Highly explosive throwable weapon";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
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
    protected void onThrownHit(final @NotNull ProjectileHitEvent event, final @NotNull Snowball thrown) {
        thrown.getLocation().createExplosion(thrown, 4F, false, true);
    }
}
