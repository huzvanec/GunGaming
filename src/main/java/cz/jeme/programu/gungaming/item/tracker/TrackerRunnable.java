package cz.jeme.programu.gungaming.item.tracker;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.util.Components;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Collection;

final class TrackerRunnable extends BukkitRunnable {
    private static boolean running = false;

    public static synchronized boolean running() {
        return running;
    }

    public TrackerRunnable() {
        running = true;
        runTaskTimer(GunGaming.plugin(), 0L, 15L);
    }

    private static final @NotNull DecimalFormat FORMATTER = new DecimalFormat("00.00");

    @Override
    public void run() {
        final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (final Player player : players) {
            final PlayerInventory inventory = player.getInventory();
            final ItemStack mainHand = inventory.getItemInMainHand();
            final ItemStack offHand = inventory.getItemInOffHand();

            final boolean isMainHand = CustomItem.is(mainHand, PlayerTracker.class);

            if (!isMainHand && !CustomItem.is(offHand, PlayerTracker.class)) {
                player.setCompassTarget(player.getWorld().getSpawnLocation());
                continue;
            }

            final ItemStack item = isMainHand ? mainHand : offHand;
            final PlayerTracker tracker = CustomItem.of(item, PlayerTracker.class);

            Player nearestPlayer = null;
            double distance = Double.MAX_VALUE;
            for (final Player trackPlayer : players) {
                if (!trackPlayer.isValid()) continue; // don't track dead players
                if (trackPlayer.getGameMode() == GameMode.SPECTATOR) continue; // don't track spectators
                if (trackPlayer.getUniqueId().equals(player.getUniqueId())) continue; // that's me lol
                if (!tracker.validate(player, trackPlayer)) continue;
                final double newDistance = player.getLocation().distance(trackPlayer.getLocation());
                if (newDistance < distance) {
                    distance = newDistance;
                    nearestPlayer = trackPlayer;
                }
            }

            final boolean hasGun = Gun.is(mainHand) || Gun.is(offHand);

            if (nearestPlayer == null) {
                if (!hasGun)
                    player.sendActionBar(Components.of("<red>There are no players to track!"));
                continue;
            }

            player.setCompassTarget(nearestPlayer.getLocation());

            if (hasGun) continue;

            final double phase = Math.min(distance / 100D, 1);

            player.sendActionBar(Components.of("<aqua>Tracking ").append(
                    nearestPlayer.teamDisplayName().append(
                            Components.of("<aqua> | <transition:#00FF00:#FFFF00:#FF0000:" + phase + ">"
                                          + FORMATTER.format(distance) + "</transition> blocks away")
                    )
            ));
        }
    }
}
