package cz.jeme.programu.gungaming.item.throwable.grenade;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public final class SmokeGrenade extends Grenade {
    public static final int DURATION = 130; // Duration of effects in ticks

    @Override
    public int getCustomModelData() {
        return 2;
    }

    @Override
    public @NotNull String getName() {
        return "Smoke Grenade";
    }

    @Override
    public @NotNull String getInfo() {
        return "Grenade that blinds enemies";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public int getMinStackLoot() {
        return 1;
    }

    @Override
    public int getMaxStackLoot() {
        return 4;
    }

    @Override
    public int getThrowCooldown() {
        return 1000;
    }

    @Override
    public double getDamage() {
        return 0D;
    }

    @Override
    protected void onThrownHit(@NotNull ProjectileHitEvent event, @NotNull Projectile thrown) {
        Location location = thrown.getLocation();
        new BukkitRunnable() {
            private int counter = 0;

            @Override
            public void run() {
                if (counter == DURATION) {
                    cancel();
                    return;
                }
                World world = location.getWorld();
                world.spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, location, 25, 2, 2, 2, 0.02);
                for (Entity entity : world.getNearbyEntities(location, 4, 4, 4)) {
                    if (!(entity instanceof LivingEntity livingEntity)) continue;
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 30, 0, false, false, false));
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 255, false, false, false));
                }
                counter++;
            }
        }.runTaskTimer(GunGaming.getPlugin(), 0L, 1L);
    }
}