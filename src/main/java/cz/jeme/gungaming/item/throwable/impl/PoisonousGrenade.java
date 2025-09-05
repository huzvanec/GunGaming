package cz.jeme.gungaming.item.throwable.impl;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.game.GameTeam;
import cz.jeme.gungaming.item.throwable.Grenade;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PoisonousGrenade extends Grenade {
    public static final int POISON_AMPLIFIER = 2;
    public static final int STAY_TICKS = 200;
    public static final int EXPAND_TICKS = 50;

    // the final amount of blocks the clouds will poison
    // this works in all direction so value '3' will produce a 6x6x6 cube
    public static final double MAX_EXPAND_BLOCKS = 3;

    private static final Color COLOR = Color.fromRGB(164, 183, 41);
    private static final Particle.DustOptions DUST_OPTIONS = new Particle.DustOptions(COLOR, 6);

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
        return Component.text("Poisonous Grenade");
    }

    @Override
    protected String provideDescription() {
        return "Releases highly poisonous clouds";
    }

    @Override
    protected Rarity provideRarity() {
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
    protected @KeyPattern.Value String provideKey() {
        return "poisonous_grenade";
    }

    @Override
    protected void onThrownHit(final ProjectileHitEvent event, final Snowball thrown) {
        final Location location = thrown.getLocation();
        final ProjectileSource shooter = thrown.getShooter();
        final Player shooterPlayer = shooter instanceof Player ? (Player) shooter : null;
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
                world.spawnParticle(Particle.DUST, location, 50, offset, offset, offset, 0.02, DUST_OPTIONS);
                for (final Entity entity : world.getNearbyEntities(location, offset, offset, offset)) {
                    if (!(entity instanceof final LivingEntity livingEntity)) continue;
                    if (
                            shooterPlayer != null &&
                            !shooterPlayer.getUniqueId().equals(entity.getUniqueId()) && // do poison self
                            GameTeam.ofPlayer(shooterPlayer).contains(entity.getUniqueId())    // don't poison teammates
                    ) continue;
                    livingEntity.addPotionEffect(new PotionEffect(
                            PotionEffectType.POISON,
                            250,
                            POISON_AMPLIFIER,
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
