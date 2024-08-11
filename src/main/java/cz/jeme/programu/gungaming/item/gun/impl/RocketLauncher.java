package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.Rocket;
import cz.jeme.programu.gungaming.item.attachment.disable.MagazineDisabled;
import cz.jeme.programu.gungaming.item.attachment.disable.SilencerDisabled;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class RocketLauncher extends Gun implements MagazineDisabled, SilencerDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 1;
    }

    @Override
    protected int provideShootCooldown() {
        return 40;
    }

    @Override
    protected int provideReloadDuration() {
        return 86;
    }

    @Override
    protected double provideDamage() {
        return 20;
    }

    @Override
    protected double provideBulletVelocity() {
        return 2;
    }

    @Override
    protected double provideRecoil() {
        return .4;
    }

    @Override
    protected double provideInaccuracy() {
        return 1;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return Rocket.class;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Rocket Launcher");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Everything goes KABOOM";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "rocket_launcher";
    }

    protected static final @NotNull Sound ROCKET_SOUND = Sound.sound(GunGaming.namespaced("entity.rocket.ambient"), Sound.Source.HOSTILE, 3F, 1F);
    protected static final @NotNull Sound EXPLOSION_SOUND = Sound.sound(GunGaming.namespaced("entity.rocket.hit"), Sound.Source.HOSTILE, 50F, 1F);

    @Override
    protected void onShoot(final @NotNull PlayerInteractEvent event, final @NotNull AbstractArrow bullet) {
        bullet.setGravity(false);
        ((SpectralArrow) bullet).setGlowingTicks(0);

        final Vector velocity = bullet.getVelocity();
        final Location location = bullet.getLocation();
        bullet.getWorld().playSound(ROCKET_SOUND, location.x(), location.y(), location.z());

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                counter++;
                bullet.setVelocity(velocity);
                final Location location = bullet.getLocation();
                final World world = bullet.getWorld();
                world.spawnParticle(Particle.LARGE_SMOKE, location.getX(), location.getY(), location.getZ(), 50, 0, 0, 0, 0.1, null);
                world.spawnParticle(Particle.LAVA, location.getX(), location.getY(), location.getZ(), 20);
                if (counter >= 100 || !bullet.isValid()) {
                    bullet.setGravity(true);
                    cancel();
                }
            }
        }.runTaskTimer(GunGaming.plugin(), 0L, 5L);
    }

    @Override
    protected void onBulletHit(final @NotNull ProjectileHitEvent event, final @NotNull AbstractArrow bullet) {
        final Location location = bullet.getLocation();
        location.createExplosion(bullet, 7F, true, true);
        final World world = bullet.getWorld();
        world.stopSound(ROCKET_SOUND);
        world.playSound(EXPLOSION_SOUND, location.x(), location.y(), location.z());
        bullet.remove();
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 4;
    }

    @Override
    protected @NotNull Class<? extends AbstractArrow> provideArrowType() {
        return SpectralArrow.class;
    }
}
