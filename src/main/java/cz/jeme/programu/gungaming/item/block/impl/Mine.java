package cz.jeme.programu.gungaming.item.block.impl;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.block.CustomBlock;
import cz.jeme.programu.gungaming.item.throwable.ThrownHelper;
import cz.jeme.programu.gungaming.item.throwable.impl.MineThrowable;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Lores;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Mine extends CustomBlock {
    public static final int MAX_DAMAGE = 30;
    public static final int ACTIVATION_TIME = 6 * 20; // ticks
    public static final double ENTITY_CHECK_RADIUS = 3; // blocks
    public static final double BULLET_CHECK_RADIUS = 3; // blocks
    private static final @NotNull Particle.DustOptions DUST_OPTIONS = new Particle.DustOptions(Color.RED, 1);

    private static final @NotNull Sound ACTIVATING_SOUND = Sound.sound(GunGaming.namespaced("block.mine.activating"), Sound.Source.BLOCK, 2, 1);
    private static final @NotNull Sound ACTIVATED_SOUND = Sound.sound(GunGaming.namespaced("block.mine.activated"), Sound.Source.BLOCK, 2, 1);
    private static final @NotNull Sound WARNING_SOUND = Sound.sound(GunGaming.namespaced("block.mine.warning"), Sound.Source.BLOCK, 2, 1);
    private static final @NotNull Sound EXPLOSION_SOUND = Sound.sound(GunGaming.namespaced("block.mine.explosion"), Sound.Source.BLOCK, 9.4F, 1);

    private static final @NotNull Queue<ActiveMine> MINES = new ConcurrentLinkedQueue<>();


    protected Mine() {
        item.editMeta(meta -> meta.setMaxStackSize(16));
        new MineCheckRunnable();
    }

    @Override
    protected @NotNull String provideDescription() {
        return "unpleasant surprise for enemies";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.POLISHED_BLACKSTONE_BUTTON;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 4;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "mine";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Mine");
    }


    @Override
    protected @NotNull List<String> update(final @NotNull ItemStack item) {
        return List.of(
                Lores.loreStat("Damage", Lores.STATS_FORMATTER.format(MAX_DAMAGE))
        );
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
    }

    @Override
    protected void onPlace(final @NotNull BlockPlaceEvent event) {
        final Block block = event.getBlock();
        block.setType(Material.AIR);
        final Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE) {
            final ItemStack item = event.getItemInHand();
            item.setAmount(item.getAmount() - 1);
        }
        final Location location = block.getLocation();
        final World world = block.getWorld();
        // particles
        final Location particleLocation = location.clone().add(.5, .5, .5);
        new BukkitRunnable() {
            private int counter = 0;

            @Override
            public void run() {
                if (counter >= ACTIVATION_TIME) {
                    MINES.add(new ActiveMine(location, player));
                    world.playSound(ACTIVATED_SOUND, particleLocation.x(), particleLocation.y(), particleLocation.z());
                    cancel();
                    return;
                }
                if (counter % 20 == 0)
                    world.playSound(ACTIVATING_SOUND, particleLocation.x(), particleLocation.y(), particleLocation.z());
                world.spawnParticle(Particle.DUST, particleLocation, 10, .2, .2, .2, 0, DUST_OPTIONS);
                counter++;
            }
        }.runTaskTimer(GunGaming.plugin(), 0L, 1L);
    }

    public static void explode(final @NotNull ActiveMine mine) {
        MINES.remove(mine);
        final Location location = mine.location();
        final World world = location.getWorld();
        world.playSound(WARNING_SOUND, location.x(), location.y(), location.z());
        world.spawnParticle(Particle.DUST, location.clone().add(.5, .5, .5), 200, .2, .2, .2, 0, DUST_OPTIONS);
        Bukkit.getScheduler().runTaskLater(
                GunGaming.plugin(),
                () -> {
                    final MineThrowable throwable = CustomElement.of(MineThrowable.class);
                    final Snowball damager = mine.placer().launchProjectile(
                            Snowball.class,
                            null,
                            snowball -> {
                                ThrownHelper.THROWABLE_KEY_DATA.write(snowball, throwable.key().asString());
                                ThrownHelper.MAX_DAMAGE_DATA.write(snowball, throwable.maxDamage());
                                snowball.setItem(throwable.item());
                                snowball.teleport(location);
                            }
                    );
                    damager.hitEntity(damager); // explode
                    damager.remove();
                    world.playSound(EXPLOSION_SOUND, location.x(), location.y(), location.z());
                },
                19
        );
    }

    public static @NotNull Queue<ActiveMine> activeMines() {
        return MINES;
    }

    private static class MineCheckRunnable extends BukkitRunnable {
        public MineCheckRunnable() {
            runTaskTimer(GunGaming.plugin(), 0L, 1L);
        }

        @Override
        public void run() {
            for (final ActiveMine mine : MINES) {
                final Location location = mine.location();
                if (location.getNearbyEntitiesByType(AbstractArrow.class, BULLET_CHECK_RADIUS).stream()
                            .anyMatch(a -> !a.isInBlock()) ||
                    location.getNearbyLivingEntities(ENTITY_CHECK_RADIUS).stream()
                            .anyMatch(e -> !(e instanceof final Player p && p.getGameMode() == GameMode.SPECTATOR))
                ) explode(mine);
            }
        }
    }

    public record ActiveMine(
            @NotNull Location location,
            @NotNull Player placer
    ) {
        @Override
        public boolean equals(final @Nullable Object o) {
            if (this == o) return true;
            if (!(o instanceof final ActiveMine that)) return false;

            return placer.equals(that.placer) && location.equals(that.location);
        }

        @Override
        public int hashCode() {
            int result = location.hashCode();
            result = 31 * result + placer.hashCode();
            return result;
        }
    }
}
