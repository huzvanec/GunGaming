package cz.jeme.programu.gungaming.item.throwable;

import cz.jeme.programu.gungaming.GlobalEventHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

public final class ThrowableEventHandler {
    private ThrowableEventHandler() {
        throw new AssertionError();
    }

    public static void onProjectileHit(final @NotNull ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof final Snowball thrown)) return;
        if (!ThrownHelper.isThrown(thrown)) return;
        ThrownHelper.getThrowable(thrown).thrownHit(event, thrown);
    }

    public static void onEntityDamageByEntity(final @NotNull EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof final Snowball thrown) || !ThrownHelper.isThrown(thrown)) {
            if (event.getDamager() instanceof Projectile)
                GlobalEventHandler.resetNoDamageTicks(event.getDamager());
            return;
        }

        final Entity hurt = event.getEntity();

        if (hurt instanceof final LivingEntity livingEntity)
            livingEntity.setMaximumNoDamageTicks(0);

        final Throwable throwable = ThrownHelper.getThrowable(thrown);
        final double distance = thrown.getLocation().distance(hurt.getLocation());
        final double maxDamage = ThrownHelper.MAX_DAMAGE_DATA.require(thrown);
        final double damage = Math.max(0, maxDamage - distance * throwable.damageDistanceRatio());
        event.setDamage(damage);
    }
}
