package cz.jeme.programu.gungaming.game;

import cz.jeme.programu.gungaming.config.GameConfig;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Random;

final class Respawn extends Countdown {
    private static final @NotNull Random RANDOM = new Random();

    private final @NotNull Player player;
    private final @NotNull Game game;
    private final @NotNull World world;

    public Respawn(final @NotNull Game game, final @NotNull Player player) {
        super(GameConfig.RESPAWN_SECONDS.get(), null);
        this.game = game;
        this.player = player;
        player.setGameMode(GameMode.SPECTATOR);
        world = player.getWorld();
        final PlayerInventory inventory = player.getInventory();
        for (final ItemStack item : inventory) {
            if (item == null) continue;
            world.dropItemNaturally(player.getLocation(), item);
        }
        inventory.clear();
    }

    @Override
    protected void tick(final long counter, final float phase) {
        if (!Game.running()) {
            cancel();
            return;
        }
        final Title title = Title.title(
                Components.of("<transition:#FF0000:#FFFF00:#00FF00:" + phase + ">" + counter),
                Components.of("<gold>" + Components.latinString("Respawning...")),
                Title.Times.times(Duration.ZERO, Duration.ofSeconds(3), Duration.ZERO)
        );
        player.showTitle(title);
    }

    private static final int BORDER_BLOCKS = 10; // minimum amount of blocks between the respawn position and the nearest border

    @Override
    protected void expire() {
        player.clearTitle();
        final World world = player.getWorld();
        final int xMin = game.xMin() + BORDER_BLOCKS;
        final int zMin = game.zMin() + BORDER_BLOCKS;
        final int xMax = game.xMax() - BORDER_BLOCKS;
        final int zMax = game.zMax() - BORDER_BLOCKS;
        respawning:
        for (int i = 0; i < 1000; i++) {
            final int x = RANDOM.nextInt(Math.abs(xMax - xMin)) + xMin;
            final int z = RANDOM.nextInt(Math.abs(zMax - zMin)) + zMin;
            for (int y = world.getMaxHeight() - 3; y > world.getMinHeight(); y--) {
                final Block block = world.getBlockAt(x, y, z);
                if (block.isEmpty() || !block.getType().isOccluding()) continue;
                final Block up1 = world.getBlockAt(x, y + 1, z);
                final Block up2 = world.getBlockAt(x, y + 2, z);
                if (!(up1.isEmpty() && up2.isEmpty())) continue respawning;
                player.teleport(up1.getLocation().add(.5, 0, .5));
                player.setGameMode(GameMode.SURVIVAL);
                return;
            }
        }
        new Respawn(game, player);
        throw new RuntimeException("Could not respawn player in 1000 attempts!");
    }
}
