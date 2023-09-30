package cz.jeme.programu.gungaming.item.misc;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.loot.SingletonLoot;
import cz.jeme.programu.gungaming.util.Message;
import cz.jeme.programu.gungaming.util.registry.Guns;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PlayerTracker extends Misc implements SingletonLoot {

    public PlayerTracker() {
        setup();
        new Updater().runTaskTimer(GunGaming.getPlugin(), 0L, 5L);
    }

    @Override
    protected void setup() {
        name = "Player Tracker";
        info = "Hold this in your hand to track the nearest player";
        rarity = Rarity.EPIC;
        customModelData = 1;
    }

    @Override
    protected @NotNull Material getMaterial() {
        return Material.COMPASS;
    }

    @Override
    public int getMinLoot() {
        return 1;
    }

    @Override
    public int getMaxLoot() {
        return 1;
    }

    private final class Updater extends BukkitRunnable {
        private static final @NotNull DecimalFormat FORMATTER = new DecimalFormat("00.00");

        @Override
        public void run() {
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            for (Player player : players) {
                PlayerInventory inv = player.getInventory();
                ItemStack mainHand = inv.getItemInMainHand();
                ItemStack offHand = inv.getItemInOffHand();

                if (!mainHand.isSimilar(item) && !offHand.isSimilar(item)) continue;

                Location location = player.getLocation();
                Player nearestPlayer = null;
                double distance = Double.MAX_VALUE;
                List<Player> trackablePlayers = players.stream()
                        .filter(Entity::isValid)
                        .filter(p -> p.getUniqueId() != player.getUniqueId())
                        .toList();
                for (Player p : trackablePlayers) {
                    Location pLocation = p.getLocation();
                    double xDiff = Math.abs(location.getX() - pLocation.getX());
                    double zDiff = Math.abs(location.getZ() - pLocation.getZ());
                    double pDistance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(zDiff, 2));
                    if (pDistance < distance) {
                        distance = pDistance;
                        nearestPlayer = p;
                    }
                }

                boolean hasGun = Guns.isGun(mainHand) || Guns.isGun(offHand);

                if (nearestPlayer == null) {
                    if (!hasGun) {
                        player.sendActionBar(Message.from("<red>There are no players to track!</red>"));
                    }
                    continue;
                }

                player.setCompassTarget(nearestPlayer.getLocation());

                if (hasGun) continue;

                double phase = Math.min(distance / 100D, 1D);

                player.sendActionBar(Message.from(
                        "<transition:#FF0000:#00FF00:" + phase
                                + ">Tracking "
                                + nearestPlayer.getName()
                                + " || " + FORMATTER.format(distance)
                                + " blocks away</transition>"
                ));
            }
        }
    }
}
