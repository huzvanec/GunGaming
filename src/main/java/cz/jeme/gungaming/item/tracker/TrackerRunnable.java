package cz.jeme.gungaming.item.tracker;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.item.armor.impl.StealthHelmet;
import cz.jeme.gungaming.util.Components;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.LodestoneTracker;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jspecify.annotations.NullMarked;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NullMarked
final class TrackerRunnable extends BukkitRunnable {
    private static boolean running = false;

    public static synchronized boolean running() {
        return running;
    }

    private final StealthHelmet stealthHelmet = CustomItem.of(StealthHelmet.class);
    private final Component stealthHelmetWarning = Components.of(
            "<red>You cannot track players while wearing a "
    ).append(stealthHelmet.name()).append(Component.text("!"));
    private static final Component NO_PLAYERS_WARNING = Components.of(
            "<red>No trackable players found!"
    );

    public TrackerRunnable() {
        running = true;
        runTaskTimer(GunGaming.instance(), 0L, 20L);
    }

    private static final DecimalFormat FORMATTER = new DecimalFormat("00.00");

    private final List<ItemStack> trackers = new ArrayList<>();

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void run() {
        final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (final Player player : players) {
            final PlayerInventory inventory = player.getInventory();

            trackers.clear();
            for (final ItemStack item : inventory)
                if (CustomItem.is(item, PlayerTracker.class))
                    trackers.add(item);

            if (trackers.isEmpty()) continue;

            final ItemStack mainHand = inventory.getItemInMainHand();
            final ItemStack offHand = inventory.getItemInOffHand();

            final boolean trackerInMainHand = CustomItem.is(mainHand, PlayerTracker.class);
            final boolean trackerInOffHand = CustomItem.is(offHand, PlayerTracker.class);
            final boolean trackerInHand = trackerInMainHand || trackerInOffHand;
            final boolean trackersInBothHands = trackerInMainHand && trackerInOffHand;

            // if the player is holding a tracker in his main hand
            // and in his off hand, disable the one in his off hand
            // to prevent action bar glitches
            if (trackersInBothHands) {
                trackers.remove(offHand);
                deactivateTracker(offHand);
            }

            final boolean hasStealthHelmet = StealthHelmet.hasEquipped(player);

            if (hasStealthHelmet) {
                for (final ItemStack trackerItem : trackers)
                    deactivateTracker(trackerItem);
                if (trackerInHand)
                    player.sendActionBar(stealthHelmetWarning);
                continue;
            }

            for (final ItemStack item : trackers) {
                final boolean inHand = mainHand == item;
                final PlayerTracker tracker = CustomItem.of(item, PlayerTracker.class);

                Player nearestPlayer = null;
                double distance = Double.MAX_VALUE;
                for (final Player trackedPlayer : players) {
                    if (!trackedPlayer.isValid()) continue; // don't track dead players
                    if (trackedPlayer.getGameMode() == GameMode.SPECTATOR) continue; // don't track spectators
                    if (trackedPlayer.getUniqueId().equals(player.getUniqueId())) continue; // that's me lol
                    if (StealthHelmet.hasEquipped(trackedPlayer)) continue; // don't track players with stealth helmets
                    if (!tracker.validate(player, trackedPlayer)) continue;
                    final double newDistance = player.getLocation().distance(trackedPlayer.getLocation());
                    if (newDistance < distance) {
                        distance = newDistance;
                        nearestPlayer = trackedPlayer;
                    }
                }

                if (nearestPlayer == null) {
                    deactivateTracker(item);
                    if (trackerInHand)
                        player.sendActionBar(NO_PLAYERS_WARNING);
                    continue;
                }

                item.setData(
                        DataComponentTypes.LODESTONE_TRACKER,
                        LodestoneTracker.lodestoneTracker(
                                nearestPlayer.getLocation(),
                                false
                        )
                );
                activateTracker(item);

                final double phase = Math.min(distance / 100D, 1);

                if (trackerInHand) {
                    player.sendActionBar(Components.of("<aqua>Tracking ").append(
                            nearestPlayer.teamDisplayName().append(
                                    Components.of("<aqua> | <transition:#00FF00:#FFFF00:#FF0000:" + phase + ">"
                                                  + FORMATTER.format(distance) + "</transition> blocks away")
                            )
                    ));
                }
            }
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private void deactivateTracker(final ItemStack item) {
        final Key key = CustomItem.of(item, PlayerTracker.class).inactiveKey();


        if (key.equals(item.getData(DataComponentTypes.ITEM_MODEL))) return;
        item.setData(
                DataComponentTypes.ITEM_MODEL,
                key
        );
    }

    @SuppressWarnings("UnstableApiUsage")
    private void activateTracker(final ItemStack item) {
        final Key key = CustomItem.of(item, PlayerTracker.class).key();

        if (key.equals(item.getData(DataComponentTypes.ITEM_MODEL))) return;
        item.setData(
                DataComponentTypes.ITEM_MODEL,
                key
        );
    }
}
