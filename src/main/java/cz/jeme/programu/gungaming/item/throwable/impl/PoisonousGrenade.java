package cz.jeme.programu.gungaming.item.throwable.impl;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.throwable.Grenade;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
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
import org.jetbrains.annotations.NotNull;

public class PoisonousGrenade extends Grenade {
    public static final int EFFECTS_DURATION = 200; // Duration of effects in ticks
    private static final @NotNull Color COLOR = Color.fromRGB(164, 183, 41);
    private static final @NotNull Particle.DustOptions DUST_OPTIONS = new Particle.DustOptions(COLOR, 6);

    @Override
    protected int provideThrowCooldown() {
        return 60;
    }

    @Override
    protected double provideMaxDamage() {
        return 0;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Poisonous Grenade");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Releases highly poisonous clouds";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
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
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "poisonous_grenade";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 10;
    }

    @Override
    protected void onThrownHit(final @NotNull ProjectileHitEvent event, final @NotNull Snowball thrown) {
        final Location location = thrown.getLocation();
        new BukkitRunnable() {
            private int counter = 0;

            @Override
            public void run() {
                if (counter == EFFECTS_DURATION) {
                    cancel();
                    return;
                }
                final double offset = 3D * counter / EFFECTS_DURATION;
                final World world = location.getWorld();
                world.spawnParticle(Particle.DUST, location, 50, offset, offset, offset, 0.02, DUST_OPTIONS);
                for (final Entity entity : world.getNearbyEntities(location, offset, offset, offset)) {
                    if (!(entity instanceof final LivingEntity livingEntity)) continue;
                    livingEntity.addPotionEffect(new PotionEffect(
                            PotionEffectType.POISON,
                            230,
                            1,
                            false,
                            false,
                            false
                    ));
                }
                counter++;
            }
        }.runTaskTimer(GunGaming.plugin(), 0L, 1L);
    }
}
