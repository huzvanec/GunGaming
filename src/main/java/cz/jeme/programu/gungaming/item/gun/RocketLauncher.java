package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Color;
import org.bukkit.Material;
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
        info = "One of the deadliest weapons in the game";
        shootCooldown = 2000;
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
                if (counter == 200) {
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