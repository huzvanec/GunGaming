package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.ammo.Rocket;
import cz.jeme.programu.gungaming.item.attachment.NoMagazine;
import cz.jeme.programu.gungaming.item.attachment.NoStock;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Sounds;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class RocketLauncher extends Gun implements NoStock, NoMagazine {

    @Override
    protected void setup() {
        name = "Rocket Launcher";
        info = "Everything goes KABOOM";
        shootCooldown = 600;
        reloadCooldown = 4300;
        damage = 20d;
        velocity = 1.5f;
        customModelData = 4;
        maxAmmo = 1;
        ammoType = Rocket.class;
        rarity = Rarity.LEGENDARY;
        recoil = 2f;
        inaccuracy = 1f;
    }

    @Override
    protected final void onShoot(@NotNull PlayerInteractEvent event, @NotNull Arrow bullet) {
        bullet.setColor(Color.fromRGB(97, 10, 0));
        bullet.setGravity(false);

        final Vector velocity = bullet.getVelocity();
        bullet.getWorld().playSound(Sounds.getSound("bullet.rocket", 2f), event.getPlayer());

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
        }.runTaskTimer(GunGaming.getPlugin(), 0L, 5L);
    }

    @Override
    public final void onBulletHit(@NotNull ProjectileHitEvent event, @NotNull Projectile bullet) {
        bullet.getWorld().createExplosion(bullet, bullet.getLocation(), 5f, true, true);
        bullet.remove();
    }
}