package cz.jeme.programu.gungaming.eventhandler;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.item.throwable.Throwable;
import cz.jeme.programu.gungaming.util.Materials;
import cz.jeme.programu.gungaming.Namespaces;
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

import java.util.List;

public class HitHandler {
    private static final List<EntityDamageEvent.DamageCause> ENTITY_CAUSES = List.of(
            EntityDamageEvent.DamageCause.ENTITY_ATTACK,
            EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK,
            EntityDamageEvent.DamageCause.PROJECTILE,
            EntityDamageEvent.DamageCause.ENTITY_EXPLOSION,
            EntityDamageEvent.DamageCause.THORNS,
            EntityDamageEvent.DamageCause.DRAGON_BREATH
    );


    public void onProjectileHit(ProjectileHitEvent event) {
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

    private void onThrownHit(ProjectileHitEvent event, Projectile thrown) {
        assert thrown instanceof ThrowableProjectile : "Thrown projectile is not a ThrowableProjectile  !";

        Throwable throwable = Throwables.getThrowable((String) Namespaces.THROWN.get(thrown));

        throwable.thrownHit(event, thrown);
    }

    private void onBulletHit(ProjectileHitEvent event, Projectile bullet) {
        assert bullet instanceof Arrow : "Projectile not Arrow!";

        Gun gun = Guns.getGun((String) Namespaces.BULLET_GUN_NAME.get(bullet));

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

    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
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

    private void onBulletDamage(EntityDamageByEntityEvent event, Arrow bullet, LivingEntity hurt) {
        hurt.setMaximumNoDamageTicks(0);

        double damage = Namespaces.BULLET_DAMAGE.get(bullet);
        event.setDamage(damage);
    }

    private void onThrownDamage(EntityDamageByEntityEvent event, Snowball thrown, LivingEntity hurt) {
        hurt.setMaximumNoDamageTicks(0);

        double damage = Namespaces.THROWN_DAMAGE.get(thrown);
        event.setDamage(damage);
    }

    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (!ENTITY_CAUSES.contains(event.getCause())) {
            resetDamageTicks(player);
        }
    }

    private static void resetDamageTicks(LivingEntity entity) {
        if (entity == null) return;
        entity.setMaximumNoDamageTicks(20);
    }
}