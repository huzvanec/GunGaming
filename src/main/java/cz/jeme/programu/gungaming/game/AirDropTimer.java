package cz.jeme.programu.gungaming.game;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.loot.crate.CrateGenerator;
import cz.jeme.programu.gungaming.loot.crate.impl.AirDrop;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

final class AirDropTimer extends BukkitRunnable {
    private final @NotNull Random random = new Random();

    private static final int MIN_MINUTES_SPAWN = 1;
    private static final int MAX_MINUTES_SPAWN = 2;
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
        boolean success = false;
        int x = 0;
        int z = 0;
        int y = 0;
        generating:
        for (int i = 0; i < 1000; i++) {
            x = random.nextInt(game.xMin() + MIN_BORDER_RADIUS, game.xMax() - MIN_BORDER_RADIUS);
            z = random.nextInt(game.zMin() + MIN_BORDER_RADIUS, game.zMax() - MIN_BORDER_RADIUS);
            for (int tempY = world.getMaxHeight() - 3; tempY > world.getMinHeight(); tempY--) {
                final Block block = world.getBlockAt(x, tempY, z);
                if (block.isEmpty() || !block.getType().isOccluding()) continue;
                final Block crateBlock = world.getBlockAt(x, tempY + 1, z);
                if (!crateBlock.isEmpty()) continue generating;
                CrateGenerator.INSTANCE.generateCrate(CustomElement.of(AirDrop.class), crateBlock.getLocation());
                y = tempY + 1;
                success = true;
                break generating;
            }
        }
        if (!success)
            throw new RuntimeException("Could not generate air drop in 1000 attempts!");

        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendRichMessage("<#FFFF00>⚠ Air drop incoming: "
                                         + x + " / " + y + " / " + z + "!");
        }
    }
}
