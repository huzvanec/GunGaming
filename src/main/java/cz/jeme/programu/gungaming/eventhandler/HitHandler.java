package cz.jeme.programu.gungaming.eventhandler;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.item.misc.GraplingHook;
import cz.jeme.programu.gungaming.item.throwable.Throwable;
import cz.jeme.programu.gungaming.util.Materials;
import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.util.item.Ammos;
import cz.jeme.programu.gungaming.util.item.Guns;
import cz.jeme.programu.gungaming.util.item.Throwables;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HitHandler {
    private static final @NotNull List<EntityDamageEvent.DamageCause> ENTITY_CAUSES = List.of(
            EntityDamageEvent.DamageCause.ENTITY_ATTACK,
            EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK,
            EntityDamageEvent.DamageCause.PROJECTILE,
            EntityDamageEvent.DamageCause.ENTITY_EXPLOSION,
            EntityDamageEvent.DamageCause.THORNS,
            EntityDamageEvent.DamageCause.DRAGON_BREATH
    );


    public void onProjectileHit(@NotNull ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if (Ammos.isBullet(projectile)) {
            onBulletHit(event, projectile);
            return;
        }
        if (Throwables.isThrown(projectile)) {
            onThrownHit(event, projectile);
            return;
        }
    }

    private void onThrownHit(@NotNull ProjectileHitEvent event, @NotNull Projectile thrown) {
        assert thrown instanceof ThrowableProjectile : "Thrown projectile is not a ThrowableProjectile  !";

        String name = Namespace.THROWN.get(thrown);
        assert name != null : "Name is null!";

        Throwable throwable = Throwables.getThrowable(name);
        assert throwable != null : "Throwable is null!";

        throwable.thrownHit(event, thrown);
    }

    private void onBulletHit(@NotNull ProjectileHitEvent event, @NotNull Projectile bullet) {
        assert bullet instanceof Arrow : "Projectile not Arrow!";

        String gunName = Namespace.BULLET_GUN_NAME.get(bullet);
        assert gunName != null : "Gun name is null!";

        Gun gun = Guns.getGun(gunName);
        assert gun != null : "Gun is null!";

        gun.bulletHit(event, bullet);

        if (event.getHitBlock() == null) {
            bullet.remove();
            return;
        }

        bullet.setGravity(true);
        bullet.setVelocity(new Vector().zero());

        Block block = event.getHitBlock();
        World world = block.getWorld();
        Material type = block.getType();

        if (Materials.isGlass(type)) {
            Location particleLocation = block.getLocation().add(0.5, 0.5, 0.5);
            world.spawnParticle(Particle.ITEM_CRACK, particleLocation, 40, 0.2, 0.2, 0.2, 0.1, new ItemStack(type));
            block.setType(Material.AIR);
            world.playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 2);
            return;
        }
        Bukkit.getScheduler().runTaskLater(GunGaming.getPlugin(), () -> {
            Location location = bullet.getLocation();
            world.spawnParticle(Particle.ITEM_CRACK, location, 5, 0, 0, 0, 0.05, new ItemStack(type));
        }, 1L);
    }

    public void onEntityDamageByEntity(@NotNull EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity hurt)) return;

        Entity damager = event.getDamager();

        if (damager.getType() == EntityType.ARROW && Ammos.isBullet((Arrow) damager)) {
            onBulletDamage(event, (Arrow) damager, hurt);
            return;
        }
        if (damager.getType() == EntityType.SNOWBALL && Throwables.isThrown((Snowball) damager)) {
            onThrownDamage(event, (Snowball) damager, hurt);
            return;
        }
        resetDamageTicks(hurt);
    }

    private void onBulletDamage(@NotNull EntityDamageByEntityEvent event, @NotNull Arrow bullet, @NotNull LivingEntity hurt) {
        hurt.setMaximumNoDamageTicks(0);

        Double damage = Namespace.BULLET_DAMAGE.get(bullet);
        assert damage != null : "Bullet damage is null!";
        event.setDamage(damage);
    }

    private void onThrownDamage(@NotNull EntityDamageByEntityEvent event, @NotNull Snowball thrown, @NotNull LivingEntity hurt) {
        hurt.setMaximumNoDamageTicks(0);

        Double damage = Namespace.THROWN_DAMAGE.get(thrown);
        assert damage != null : "Thrown damage is null!";
        event.setDamage(damage);
    }

    public void onEntityDamage(@NotNull EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        Long grappleSubtract = Namespace.GRAPPLE_LAST_SUBTRACT.get(player);
        if (grappleSubtract != null && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (System.currentTimeMillis() - grappleSubtract <= GraplingHook.FALL_RESISTANCE_MILLIS) {
                event.setCancelled(true);
            }
        }
        if (!ENTITY_CAUSES.contains(event.getCause())) {
            resetDamageTicks(player);
        }
    }

    private static void resetDamageTicks(@NotNull LivingEntity entity) {
        entity.setMaximumNoDamageTicks(20);
    }
}