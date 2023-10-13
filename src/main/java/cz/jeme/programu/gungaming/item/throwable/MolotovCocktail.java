package cz.jeme.programu.gungaming.item.throwable;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

public final class MolotovCocktail extends Throwable {
    @Override
    public int getCustomModelData() {
        return 4;
    }

    @Override
    public @NotNull String getName() {
        return "Molotov Cocktail";
    }

    @Override
    public @NotNull String getInfo() {
        return "Spreads fire everywhere";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public int getMinStackLoot() {
        return 1;
    }

    @Override
    public int getMaxStackLoot() {
        return 4;
    }

    @Override
    public int getThrowCooldown() {
        return 500;
    }

    @Override
    public double getDamage() {
        return 1D;
    }

    @Override
    protected void onThrownHit(@NotNull ProjectileHitEvent event, @NotNull Projectile thrown) {
        thrown.getWorld().createExplosion(thrown, thrown.getLocation(), 5f, true, false);
    }
}