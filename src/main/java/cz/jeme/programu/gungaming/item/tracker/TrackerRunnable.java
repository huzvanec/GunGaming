package cz.jeme.programu.gungaming.item.tracker;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.armor.impl.StealthHelmet;
import cz.jeme.programu.gungaming.util.Components;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.Collection;

final class TrackerRunnable extends BukkitRunnable {
    private static boolean running = false;

    public static synchronized boolean running() {
        return running;
    }

    public TrackerRunnable() {
        running = true;
        runTaskTimer(GunGaming.plugin(), 0L, 20L);
    }

    private static final @NotNull DecimalFormat FORMATTER = new DecimalFormat("00.00");

    @Override
    public void run() {
        final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (final Player player : players) {
            final PlayerInventory inventory = player.getInventory();

            final ItemStack mainHand = inventory.getItemInMainHand();
            final boolean isMainHand = CustomItem.is(mainHand, PlayerTracker.class);
            final ItemStack offHand = inventory.getItemInOffHand();
            final boolean isOffHand = CustomItem.is(offHand, PlayerTracker.class);

            final boolean hasPhantomHat = StealthHelmet.hasEquipped(player);

            if (hasPhantomHat || !(isMainHand || isOffHand)) {
                if (hasPhantomHat && (isMainHand || isOffHand))
                    player.sendActionBar(StealthHelmet.WARNING);
                player.setCompassTarget(player.getWorld().getSpawnLocation());
                for (final ItemStack invItem : inventory) resetTracker(invItem);
                continue;
            }

            final ItemStack item = isMainHand ? mainHand : offHand;
            for (final ItemStack invItem : inventory) {
                if (invItem == item) continue;
                resetTracker(invItem);
            }
            final PlayerTracker tracker = CustomItem.of(item, PlayerTracker.class);

            Player nearestPlayer = null;
            double distance = Double.MAX_VALUE;
            for (final Player trackPlayer : players) {
                if (!trackPlayer.isValid()) continue; // don't track dead players
                if (trackPlayer.getGameMode() == GameMode.SPECTATOR) continue; // don't track spectators
                if (trackPlayer.getUniqueId().equals(player.getUniqueId())) continue; // that's me lol
                if (StealthHelmet.hasEquipped(trackPlayer)) continue; // don't track phantom hats
                if (!tracker.validate(player, trackPlayer)) continue;
                final double newDistance = player.getLocation().distance(trackPlayer.getLocation());
                if (newDistance < distance) {
                    distance = newDistance;
                    nearestPlayer = trackPlayer;
                }
            }

            if (nearestPlayer == null) {
                player.sendActionBar(Components.of("<red>There are no players to track!"));
                continue;
            }

            player.setCompassTarget(nearestPlayer.getLocation());
            item.editMeta(meta -> {
                PlayerTracker.TRACKER_ACTIVE_DATA.write(meta, true);
                meta.setCustomModelData(tracker.activeCustomModelData());
            });

            final double phase = Math.min(distance / 100D, 1);

            player.sendActionBar(Components.of("<aqua>Tracking ").append(
                    nearestPlayer.teamDisplayName().append(
                            Components.of("<aqua> | <transition:#00FF00:#FFFF00:#FF0000:" + phase + ">"
                                          + FORMATTER.format(distance) + "</transition> blocks away")
                    )
            ));
        }
    }

    private void resetTracker(final @Nullable ItemStack item) {
        if (!PlayerTracker.TRACKER_ACTIVE_DATA.read(item).orElse(false)) return;
        assert item != null;
        final PlayerTracker tracker = CustomItem.of(item, PlayerTracker.class);
        item.editMeta(meta -> {
            PlayerTracker.TRACKER_ACTIVE_DATA.write(meta, false);
            meta.setCustomModelData(tracker.inactiveCustomModelData());
        });
    }
}
