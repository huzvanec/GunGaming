package cz.jeme.programu.gungaming.item.impl;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.loot.SingleLoot;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Collection;

public class PlayerTracker extends CustomItem implements SingleLoot {

    protected PlayerTracker() {
        new Updater().runTaskTimer(GunGaming.plugin(), 0L, 15L);
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Player Tracker");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Hold this in your hand to track the nearest player";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.COMPASS;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
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
        return "player_tracker";
    }

    private static final class Updater extends BukkitRunnable {
        private static final @NotNull DecimalFormat FORMATTER = new DecimalFormat("00.00");

        @Override
        public void run() {
            final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
            for (final Player player : players) {
                final PlayerInventory inventory = player.getInventory();
                final ItemStack mainHand = inventory.getItemInMainHand();
                final ItemStack offHand = inventory.getItemInOffHand();

                if (!CustomItem.is(mainHand, PlayerTracker.class) && !CustomItem.is(offHand, PlayerTracker.class)) {
                    player.setCompassTarget(player.getWorld().getSpawnLocation());
                    continue;
                }

                Player nearestPlayer = null;
                double distance = Double.MAX_VALUE;
                for (final Player trackPlayer : Bukkit.getOnlinePlayers()) {
                    if (!trackPlayer.isValid()) continue;
                    if (trackPlayer.getUniqueId().equals(player.getUniqueId())) continue;
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

                player.sendActionBar(Components.of(
                        "<aqua>Tracking <red>" + nearestPlayer.getName()
                        + "</red> | <transition:#FF0000:#FFFF00:#00FF00:" + phase + ">"
                        + FORMATTER.format(distance) + "</transition> blocks away"
                ));
            }
        }
    }
}
