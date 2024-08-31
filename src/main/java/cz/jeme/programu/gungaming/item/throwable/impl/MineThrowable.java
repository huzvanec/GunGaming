package cz.jeme.programu.gungaming.item.throwable.impl;

import cz.jeme.programu.gungaming.item.block.impl.Mine;
import cz.jeme.programu.gungaming.item.throwable.Throwable;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Lores;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MineThrowable extends Throwable {
    @Override
    protected int provideThrowCooldown() {
        return 40;
    }

    @Override
    protected double provideMaxDamage() {
        return Mine.MAX_DAMAGE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Mine Throwable");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Throwable used for the Mine explosion";
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
        return "mine_throwable";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 11;
    }

    @Override
    protected void onThrownHit(final @NotNull ProjectileHitEvent event, final @NotNull Snowball thrown) {
        thrown.getLocation().createExplosion(thrown, 7F, true, true);
    }
}
