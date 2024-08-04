package cz.jeme.programu.gungaming.game;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.loot.crate.CrateGenerator;
import cz.jeme.programu.gungaming.loot.crate.impl.AirDrop;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

final class AirDropTimer extends BukkitRunnable {
    private final @NotNull Random random = new Random();

    private static final int MIN_MINUTES_SPAWN = 2;
    private static final int MAX_MINUTES_SPAWN = 7;
    private static final int MIN_BORDER_RADIUS = 10;

    private final @NotNull Game game;
    private final @NotNull World world;

    public AirDropTimer(final @NotNull Game game) {
        this.game = game;
        this.world = game.world();
    }

    public void start() {
        final long duration = random.nextLong(MIN_MINUTES_SPAWN, MAX_MINUTES_SPAWN) * 1200;
        runTaskTimer(GunGaming.plugin(), duration, duration);
    }

    @Override
    public void run() {
        while (true) {
            final int x = random.nextInt(game.xMin() + MIN_BORDER_RADIUS, game.xMax() - MIN_BORDER_RADIUS);
            final int z = random.nextInt(game.zMin() + MIN_BORDER_RADIUS, game.zMax() - MIN_BORDER_RADIUS);
            for (int y = world.getMaxHeight() - 3; y > world.getMinHeight(); y--) {
                final Block block = world.getBlockAt(x, y, z);
                if (block.isEmpty() || !block.getType().isOccluding()) continue;
                final Block crateBlock = world.getBlockAt(x, y + 1, z);
                CrateGenerator.INSTANCE.generateCrate(CustomElement.of(AirDrop.class), crateBlock.getLocation());
                return;
            }
        }
    }
}
