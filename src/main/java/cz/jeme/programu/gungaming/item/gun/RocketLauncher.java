package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RocketLauncher extends Gun {

    @Override
    protected void setup() {
        name = "Rocket Launcher";
        info = "Everything goes KABOOM";
        shootCooldown = 1000;
        reloadCooldown = 7000;
        damage = 15d;
        velocity = 1.5f;
        material = Material.DIAMOND_HOE;
        maxAmmo = 1;
        ammoName = "Rocket";
        rarity = Rarity.LEGENDARY;
        minLoot = 1;
        maxLoot = 1;
    }

    @Override
    protected final void onShoot(PlayerInteractEvent event, Arrow bullet) {
        bullet.setColor(Color.fromRGB(97, 10, 0));
        bullet.setGravity(false);

        final Vector velocity = bullet.getVelocity();

        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                counter++;
                bullet.setVelocity(velocity);
                Location location = bullet.getLocation();
                World world = bullet.getWorld();
                world.spawnParticle(Particle.SMOKE_LARGE, location.getX(), location.getY(), location.getZ(), 30, 0, 0, 0, 0.1, null);
                world.spawnParticle(Particle.LAVA, location.getX(), location.getY(), location.getZ(), 10);
                if (counter == 200 || !bullet.isValid()) {
                    bullet.setGravity(true);
                    cancel();
                }
            }
        }.runTaskTimer(GunGaming.getPlugin(), 0L, 1L);
    }

    @Override
    public final void onBulletHit(ProjectileHitEvent event, Projectile bullet) {
        bullet.getWorld().createExplosion(bullet, bullet.getLocation(), 5f, true, true);
        bullet.remove();
    }
}