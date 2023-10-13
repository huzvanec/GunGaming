package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.ammo.Ammo;
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

public final class RocketLauncher extends Gun implements NoStock, NoMagazine {

    @Override
    public int getShootCooldown() {
        return 600;
    }

    @Override
    public int getReloadCooldown() {
        return 4300;
    }

    @Override
    public double getDamage() {
        return 20D;
    }

    @Override
    public float getVelocity() {
        return 1.5f;
    }

    @Override
    public int getMaxAmmo() {
        return 1;
    }

    @Override
    public @NotNull Class<? extends Ammo> getAmmoType() {
        return Rocket.class;
    }

    @Override
    public float getRecoil() {
        return 2f;
    }

    @Override
    public float getInaccuracy() {
        return 1f;
    }

    @Override
    public int getCustomModelData() {
        return 4;
    }

    @Override
    public @NotNull String getName() {
        return "Rocket Launcher";
    }

    @Override
    public @NotNull String getInfo() {
        return "Everything goes KABOOM";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.LEGENDARY;
    }


    @Override
    protected void onShoot(@NotNull PlayerInteractEvent event, @NotNull Arrow bullet) {
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
    protected void onBulletHit(@NotNull ProjectileHitEvent event, @NotNull Projectile bullet) {
        bullet.getWorld().createExplosion(bullet, bullet.getLocation(), 5f, true, true);
        bullet.remove();
    }
}