package cz.jeme.programu.gungaming.item.throwable.impl;

import cz.jeme.programu.gungaming.item.throwable.Throwable;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Snowball;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class WindPearl extends Throwable {
    @Override
    protected int provideThrowCooldown() {
        return 30;
    }

    @Override
    protected double provideMaxDamage() {
        return 0;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Wind Pearl");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "throw this pearl to fly";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 2;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "wind_pearl";
    }

    @Override
    protected void onThrow(final @NotNull PlayerInteractEvent event, final @NotNull Snowball thrown) {
        thrown.addPassenger(event.getPlayer());
        thrown.setVelocity(thrown.getVelocity().multiply(1.5));
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 6;
    }
}
