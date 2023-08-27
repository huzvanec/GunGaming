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

public class SmokeGrenade extends Grenade {
    @Override
    protected void setup() {
        name = "Smoke Grenade";
        info = "throwable weapon that blinds enemies";
        customModelData = 2;
        rarity = Rarity.UNCOMMON;
        throwCooldown = 3000;
        damage = 0d;
    }

    @Override
    public int getMinLoot() {
        return 1;
    }

    @Override
    public int getMaxLoot() {
        return 6;
    }

    @Override
    public void onThrownHit(@NotNull ProjectileHitEvent event, @NotNull Projectile thrown) {
        Location location = thrown.getLocation();
        new BukkitRunnable() {
            private int counter = 0;
            @Override
            public void run() {
                if (counter == 130) {
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
