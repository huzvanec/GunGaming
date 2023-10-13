package cz.jeme.programu.gungaming.item.throwable.grenade;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public final class MIRVGrenade extends Grenade {
    public static final double HORIZONTAL_POWER = 0.3f;
    public static final double VERTICAL_POWER = 0.3f;

    @Override
    public int getCustomModelData() {
        return 3;
    }

    @Override
    public @NotNull String getName() {
        return "MIRV Grenade";
    }

    @Override
    public @NotNull String getInfo() {
        return "Explodes into eight small grenades";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public int getMinStackLoot() {
        return 1;
    }

    @Override
    public int getMaxStackLoot() {
        return 2;
    }

    @Override
    public int getThrowCooldown() {
        return 2000;
    }

    @Override
    public double getDamage() {
        return 26D;
    }

    @Override
    protected void onThrownHit(@NotNull ProjectileHitEvent event, @NotNull Projectile thrown) {
        thrown.getWorld().createExplosion(thrown, thrown.getLocation(), 4f, false, true);
        for (int i = 0; i < 360; i += 45) {
            shootSmallGrenade(thrown, i);
        }
    }

    private void shootSmallGrenade(@NotNull Projectile thrown, double degrees) {
        final double radians = Math.toRadians(degrees);
        final double x = Math.cos(radians) * HORIZONTAL_POWER;
        final double z = Math.sin(radians) * HORIZONTAL_POWER;
        Snowball smallGrenade;
        if (!(thrown.getShooter() instanceof Player player)) {
            smallGrenade = thrown.getWorld().spawn(thrown.getLocation(), Snowball.class);
        } else {
            smallGrenade = player.launchProjectile(Snowball.class);
            smallGrenade.teleport(thrown.getLocation());
        }

        Namespace.THROWN.set(smallGrenade, "Small Grenade");
        Namespace.THROWN_DAMAGE.set(smallGrenade, SmallGrenade.DAMAGE);
        smallGrenade.setVelocity(new Vector(x, VERTICAL_POWER, z));
    }
}
