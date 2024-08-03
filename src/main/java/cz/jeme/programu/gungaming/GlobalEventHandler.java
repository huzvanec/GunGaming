package cz.jeme.programu.gungaming;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public final class GlobalEventHandler {
    private GlobalEventHandler() {
        throw new AssertionError();
    }

    public static void onEntityDamage(final @NotNull EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) return;
        resetNoDamageTicks(event.getEntity());
    }

    public static void resetNoDamageTicks(final @NotNull Entity entity) {
        if (!(entity instanceof final LivingEntity livingEntity)) return;
        livingEntity.setMaximumNoDamageTicks(20);
    }
}
