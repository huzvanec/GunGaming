package cz.jeme.programu.gungaming.item.throwable.grenade;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

public final class FragGrenade extends Grenade {

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public @NotNull String getName() {
        return "Frag Grenade";
    }

    @Override
    public @NotNull String getInfo() {
        return "Explosive throwable weapon";
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
        return 1000;
    }

    @Override
    public double getDamage() {
        return 23D;
    }

    @Override
    protected void onThrownHit(@NotNull ProjectileHitEvent event, @NotNull Projectile thrown) {
        thrown.getWorld().createExplosion(thrown, thrown.getLocation(), 3f, false, true);
    }
}