package cz.jeme.programu.gungaming.loot.crate;

import cz.jeme.programu.gungaming.ElementManager;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

final class Generation extends BukkitRunnable {
    private static final @NotNull DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("00.00");
    private final @NotNull Audience audience;
    private final int xMin;
    private final int xMax;
    private final int zMin;
    private final int zMax;
    private final int bps;
    private int x;
    private int z;
    private final @NotNull World world;
    private final @NotNull Set<Crate> crates = ElementManager.INSTANCE.elements().stream()
            .filter(Crate.class::isInstance)
            .map(Crate.class::cast)
            .collect(Collectors.toSet());
    private final long startTime;
    private int cratesCount = 0;
    private final @NotNull Collection<? extends Player> players = Bukkit.getOnlinePlayers();

    public Generation(final @NotNull Audience audience, final @NotNull Location location, final int x1, final int z1, final int x2, final int z2, final int bps) {
        this.audience = audience;
        this.bps = bps;
        xMin = Math.min(x1, x2);
        xMax = Math.max(x1, x2);
        zMin = Math.min(z1, z2);
        zMax = Math.max(z1, z2);
        x = xMin;
        z = zMin;
        world = location.getWorld();
        startTime = System.currentTimeMillis();
        runTaskTimer(GunGaming.plugin(), 0L, 1L);
    }

    @Override
    public void run() {
        for (int i = 0; i < bps; i++) {
            for (final Crate crate : crates) generate(crate, x, z);
            if (x < xMax) x++;
            else if (z == zMax) {
                cancel();
                return;
            } else {
                x = xMin;
                z++;
            }
        }
        // action bar info
        final int xPositive = Math.abs(xMin);
        final int zPositive = Math.abs(zMin);
        final int currentBlocks = (xMax + xPositive) * (z + zPositive) + (x + xPositive);
        final int allBlocks = (xMax + xPositive) * (zMax + zPositive);
        final double progress = Math.min((double) currentBlocks / allBlocks, 1);
        final String percentageStr = "<transition:#FF0000:#FFFF00:#00FF00:" + progress + ">"
                                     + DECIMAL_FORMATTER.format(progress * 100D)
                                     + "%</transition>";

        final long currentTime = System.currentTimeMillis();
        final int remainingBlocks = allBlocks - currentBlocks;
        final double speed = 1000D * currentBlocks / (currentTime - startTime); // blocks per second
        int seconds = (int) (remainingBlocks / speed);
        final int hours = seconds / 3600;
        final int minutes = seconds % 3600 / 60;
        seconds %= 60;
        final String hoursStr = hours == 0 ? "" : hours + "hrs ";
        final String minutesStr = minutes == 0 ? "" : minutes + "min ";
        final String secondsStr = seconds + "sec ";
        final String etaStr = hoursStr + minutesStr + secondsStr + "remaining";
        for (final Player player : players)
            player.sendActionBar(Components.of("<green>Generating: " + percentageStr + " | " + etaStr));
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        audience.sendActionBar(Component.empty());
        if (x == xMax && z == zMax)
            audience.sendMessage(Components.prefix("<green>Successfully generated " + cratesCount + " crates"));
        CrateGenerator.INSTANCE.generation = null;
        super.cancel();
    }

    private final @NotNull Random random = new Random();

    private void generate(final @NotNull Crate crate, final int x, final int z) {
        final List<Integer> heights = new ArrayList<>();
        boolean lastOccluding = false;
        for (int y = world.getMinHeight(); y < world.getMaxHeight() - 1; y++) {
            final Material material = world.getBlockAt(x, y, z).getType();
            if (material.isOccluding()) {
                lastOccluding = true;
                continue;
            }
            if (lastOccluding && material.isEmpty()) {
                heights.add(y);
            }
            lastOccluding = false;
        }
        if (heights.isEmpty()) return;
        for (final int y : heights) {
            if (random.nextDouble() > crate.spawnPercentage()) continue;
            CrateGenerator.INSTANCE.generateCrate(crate, world.getBlockAt(x, y, z));
            cratesCount++;
        }
    }
}
