package cz.jeme.programu.gungaming.item.throwable.impl;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.throwable.Throwable;
import cz.jeme.programu.gungaming.item.throwable.ThrownHelper;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AirStrikeStrobe extends Throwable {

    protected AirStrikeStrobe() {
        item.editMeta(meta -> meta.setMaxStackSize(1));
    }

    @Override
    protected int provideThrowCooldown() {
        return 60;
    }

    @Override
    protected double provideMaxDamage() {
        return 1;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Calls an air strike where it's thrown";
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 1;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "air_strike_strobe";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Air Strike Strobe");
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 7;
    }

    @Override
    protected @NotNull List<String> update(final @NotNull ItemStack item) {
        return List.of();
    }

    private static final @NotNull Sound PLANE_SOUND = Sound.sound(GunGaming.namespaced("entity.air_strike.ambient"), Sound.Source.MASTER, 9.4F, 1);

    private static final int BOMBS = 25; // the amount of bombs that is dropped in one wave
    private static final int BOMB_SPACING = 2; // spacing between separate bombs in blocks
    private static final int BOMB_Y_LEVEL = 320; // the y level the bombs are spawned at
    private static final long BOMB_TIME = 1; // the amount of ticks between separate bombs being spawned
    private static final int BOMB_WAVES = 3;
    private static final long BOMB_WAVE_TIME = 30; // the amount of ticks between separate bomb waves
    private static final double BOMB_FORCE = 5; // the force used to drop the bombs down
    private static final long BOMB_DELAY = 5 * 20; // the amount of ticks before the bombs start spawning
    private static final double BOMB_INACCURACY = 1;
    private static final int CENTER_OFFSET = 5; // the offset of the bombing center (forwards) in blocks

    private static final @NotNull Random RANDOM = new Random();

    private static double nextAxis(final double radians) {
        return RANDOM.nextDouble(radians * 2) - radians;
    }

    private static void randomizeVector(final @NotNull Vector vector) {
        final double rad = Math.toRadians(BOMB_INACCURACY);
        vector.rotateAroundX(nextAxis(rad));
        vector.rotateAroundZ(nextAxis(rad));
    }

    @Override
    protected void onThrownHit(final @NotNull ProjectileHitEvent event, final @NotNull Snowball thrown) {
        final Location location = thrown.getLocation();
        final World world = thrown.getWorld();
        final Vector direction = location.getDirection();
        final Snowball indicator = world.spawn(location, Snowball.class, snowball -> {
            snowball.setGravity(false);
            snowball.setItem(item);
        });

        direction.setX(-direction.getX()); // very nice! x is swapped in directions for some reason
        direction.multiply(BOMB_SPACING / Math.max(Math.abs(direction.getX()), Math.abs(direction.getZ())));
        final double xForce = direction.getX();
        final double zForce = direction.getZ();

        final int multiplierEnd = BOMBS / 2 + CENTER_OFFSET;
        final int multiplierStart = multiplierEnd - BOMBS;

        final AirStrikeBomb bomb = CustomElement.of(AirStrikeBomb.class);
        final ItemStack bombItem = bomb.item();
        final ProjectileSource shooter = Objects.requireNonNull(thrown.getShooter(), "Shooter is null!");
        // particles
        new BukkitRunnable() {
            private long counter = 0;

            @Override
            public void run() {
                if (counter >= BOMB_DELAY) {
                    cancel();
                    return;
                }
                final double offsetY = 10;
                final Location particleLocation = location.clone().add(0, offsetY, 0);
                world.spawnParticle(Particle.DUST, particleLocation, 10, .5, offsetY, .5, 0, new Particle.DustOptions(Color.RED, 3));
                counter++;
            }
        }.runTaskTimer(GunGaming.plugin(), 0L, 1L);
        // bombs
        for (int i = 0; i < BOMB_WAVES; i++) {
            final long delay = i * BOMB_WAVE_TIME + BOMB_DELAY;
            new BukkitRunnable() {
                private int multiplier = multiplierStart;

                @Override
                public void run() {
                    if (multiplier >= multiplierEnd) {
                        cancel();
                        return;
                    }
                    if (multiplier == multiplierStart) {
                        if (indicator.isValid()) indicator.remove();
                        world.playSound(PLANE_SOUND, location.x(), location.y(), location.z());
                    }
                    final double xOffset = xForce * multiplier;
                    final double zOffset = zForce * multiplier;
                    final Vector vector = new Vector(0, -BOMB_FORCE, 0);
                    randomizeVector(vector);
                    vector.setY(-BOMB_FORCE); // reapply the y force after the randomization
                    shooter.launchProjectile(
                            Snowball.class, vector, snowball -> {
                                final Location bombLocation = location.clone();
                                bombLocation.add(xOffset, 0, zOffset).setY(BOMB_Y_LEVEL);
                                snowball.teleport(bombLocation);

                                ThrownHelper.THROWABLE_KEY_DATA.write(snowball, bomb.key().asString());
                                ThrownHelper.MAX_DAMAGE_DATA.write(snowball, MAX_DAMAGE_DATA.require(bombItem));
                                snowball.setItem(bombItem);
                            }
                    );
                    multiplier++;
                }
            }.runTaskTimer(GunGaming.plugin(), delay, BOMB_TIME);
        }
    }
}