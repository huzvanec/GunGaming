package cz.jeme.programu.gungaming.game.runnable;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.config.GameConfig;
import cz.jeme.programu.gungaming.game.Game;
import cz.jeme.programu.gungaming.loot.crate.CrateGenerator;
import cz.jeme.programu.gungaming.loot.crate.impl.AirDrop;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@ApiStatus.Internal
public final class AirDropRunnable extends GameRunnable {
    private final @NotNull Random random = ThreadLocalRandom.current();

    public static final @NotNull Sound AIR_DROP_AMBIENT_SOUND = Sound.sound(GunGaming.namespaced("game.air_drop"), Sound.Source.MASTER, 1, 1);
    public static final @NotNull Sound AIR_DROP_PING_SOUND = Sound.sound(GunGaming.namespaced("game.ping.air_drop"), Sound.Source.MASTER, 1, 1);

    private static final int MIN_BORDER_RADIUS = 10;

    private final @NotNull Game game;
    private final @NotNull World world;

    public AirDropRunnable(final @NotNull Game game) {
        this.game = game;
        this.world = game.world();
        final long duration = random.nextLong(GameConfig.AIR_DROP_MIN_SECONDS.get(), GameConfig.AIR_DROP_MAX_SECONDS.get()) * 20;
        runTaskLater(GunGaming.plugin(), duration);
    }

    public void spawnAirDrop() {
        if (!Game.running()) return;
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
        new AirDropRunnable(game);
        if (!success)
            throw new RuntimeException("Could not generate air drop in 1000 attempts!");

        for (final Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(AIR_DROP_PING_SOUND, player);
            player.sendRichMessage("<#FFFF00>⚠ Air drop incoming: "
                                   + x + " / " + y + " / " + z + "!");
        }
    }

    @Override
    public void run() {
        for (final Player player : Bukkit.getOnlinePlayers())
            player.playSound(AIR_DROP_AMBIENT_SOUND, player);
        Bukkit.getScheduler().runTaskLater(
                GunGaming.plugin(),
                this::spawnAirDrop,
                220
        );
    }
}
