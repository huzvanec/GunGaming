package cz.jeme.programu.gungaming.item.throwable.grenade;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

public final class SmallGrenade extends Grenade {
    public static final double DAMAGE = 10D;

    @Override
    public int getCustomModelData() {
        return 0;
    }

    @Override
    public @NotNull String getName() {
        return "Small Grenade";
    }

    @Override
    public @NotNull String getInfo() {
        return "Grenade used for the small MIRV explosion";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.UNOBTAINABLE;
    }

    @Override
    public int getMinStackLoot() {
        return 0;
    }

    @Override
    public int getMaxStackLoot() {
        return 0;
    }

    @Override
    public int getThrowCooldown() {
        return 800;
    }

    @Override
    public double getDamage() {
        return DAMAGE;
    }

    @Override
    protected void onThrownHit(@NotNull ProjectileHitEvent event, @NotNull Projectile thrown) {
        thrown.getWorld().createExplosion(thrown, thrown.getLocation(), 2f, false, true);
    }
}