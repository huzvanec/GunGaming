package cz.jeme.gungaming.item.throwable.impl;

import cz.jeme.gungaming.item.throwable.Throwable;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Snowball;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
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
    protected Component provideName() {
        return Component.text("Wind Pearl");
    }

    @Override
    protected String provideDescription() {
        return "throw this pearl to fly";
    }

    @Override
    protected Rarity provideRarity() {
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
    protected @KeyPattern.Value String provideKey() {
        return "wind_pearl";
    }

    @Override
    protected void onThrow(final PlayerInteractEvent event, final Snowball thrown) {
        thrown.addPassenger(event.getPlayer());
        thrown.setVelocity(thrown.getVelocity().multiply(2));
    }

    @Override
    protected List<String> update(final ItemStack item) {
        return List.of();
    }

}
