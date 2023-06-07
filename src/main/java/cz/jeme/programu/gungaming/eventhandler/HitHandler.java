package cz.jeme.programu.gungaming.eventhandler;

import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.util.Materials;
import cz.jeme.programu.gungaming.util.Namespaces;
import cz.jeme.programu.gungaming.util.item.Ammos;
import cz.jeme.programu.gungaming.util.item.Guns;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
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
        Projectile bullet = event.getEntity();

        if (!Ammos.isBullet(bullet)) return;

        assert bullet instanceof Arrow : "Projectile not Arrow!";

        Gun gun = Guns.getGun((String) Namespaces.BULLET_GUN_NAME.get(bullet));

        gun.onBulletHit(event, bullet);

        if (event.getHitBlock() == null) {
            bullet.remove();
            return;
        }

        bullet.setGravity(true);

        Block block = event.getHitBlock();
        Material type = block.getType();
        if (Arrays.asList(Materials.GLASSES).contains(type) || Arrays.asList(Materials.GLASS_PANES).contains(type)) {
            block.setType(Material.AIR);
            for (Player player : Bukkit.getOnlinePlayers()) {
                Location particleLocation = block.getLocation().add(0.5, 0.5, 0.5);
                player.spawnParticle(Particle.ITEM_CRACK, particleLocation, 40, 0.2, 0.2, 0.2, 0.1,
                        new ItemStack(type));
                player.playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 2);
            }
            bullet.remove();
        }
    }

    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        LivingEntity livingEntity = null;
        if (entity instanceof LivingEntity) {
            livingEntity = (LivingEntity) entity;
        }

        Entity damager = event.getDamager();

        if (damager.getType() != EntityType.ARROW) {
            resetDamageTicks(livingEntity);
            return;
        }

        Projectile bullet = (Projectile) damager;

        if (!Ammos.isBullet(bullet)) {
            resetDamageTicks(livingEntity);
            return;
        }

        if (livingEntity != null) {
            livingEntity.setMaximumNoDamageTicks(0);
        }

        double damage = Namespaces.BULLET_DAMAGE.get(bullet);

        bullet.remove();

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