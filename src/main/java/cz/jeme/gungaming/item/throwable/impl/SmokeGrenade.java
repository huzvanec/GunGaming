package cz.jeme.gungaming.item.throwable.impl;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.throwable.Grenade;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class SmokeGrenade extends Grenade {
    public static final int STAY_TICKS = 400;
    public static final int EXPAND_TICKS = 100;

    // the final amount of blocks the clouds will poison
    // this works in all direction so value '3' will produce a 6x6x6 cube
    public static final double MAX_EXPAND_BLOCKS = 3.5;

    @Override
    protected int provideThrowCooldown() {
        return 60;
    }

    @Override
    protected double provideMaxDamage() {
        return 0;
    }

    @Override
    protected Component provideName() {
        return Component.text("Smoke Grenade");
    }

    @Override
    protected String provideDescription() {
        return "blinds enemies, provides good cover";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 2;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "smoke_grenade";
    }

    @Override
    protected void onThrownHit(final ProjectileHitEvent event, final Snowball thrown) {
        final Location location = thrown.getLocation();
        new BukkitRunnable() {
            private int counter = 0;

            @Override
            public void run() {
                if (counter == STAY_TICKS) {
                    cancel();
                    return;
                }
                final double offset = MAX_EXPAND_BLOCKS * Math.min(1, (double) counter / EXPAND_TICKS);
                final World world = location.getWorld();
                world.spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, location, 50, offset, offset, offset, 0.02);
                for (final Entity entity : world.getNearbyEntities(location, offset, offset, offset)) {
                    if (!(entity instanceof final LivingEntity livingEntity)) continue;
                    livingEntity.addPotionEffect(new PotionEffect(
                            PotionEffectType.INVISIBILITY,
                            30,
                            255,
                            false,
                            false,
                            false
                    ));
                    livingEntity.addPotionEffect(new PotionEffect(
                            PotionEffectType.BLINDNESS,
                            200,
                            255,
                            false,
                            false,
                            false
                    ));
                    livingEntity.addPotionEffect(new PotionEffect(
                            PotionEffectType.DARKNESS,
                            200,
                            255,
                            false,
                            false,
                            false
                    ));
                }
                counter++;
            }
        }.runTaskTimer(GunGaming.instance(), 0L, 1L);
    }
}
