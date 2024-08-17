package cz.jeme.programu.gungaming.game.runnable.countdown;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.config.GameConfig;
import cz.jeme.programu.gungaming.game.Game;
import cz.jeme.programu.gungaming.game.GameTeam;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@ApiStatus.Internal
public final class Respawn extends Countdown {
    private final @NotNull ThreadLocalRandom random = ThreadLocalRandom.current();
    private final @NotNull Player player;
    private final @NotNull Game game;

    public Respawn(final @NotNull Game game, final @NotNull Player player) {
        super(GameConfig.RESPAWN_SECONDS.get(), null);
        this.game = game;
        this.player = player;
        player.setGameMode(GameMode.SPECTATOR);
        player.clearActivePotionEffects();
        player.getInventory().setHeldItemSlot(0);
    }

    @Override
    protected void tick(final long counter, final float phase) {
        final Title title = Title.title(
                Components.of("<transition:#FF0000:#FFFF00:#00FF00:" + phase + ">" + counter),
                Components.of("<gold>" + Components.latinString("Respawning...")),
                Title.Times.times(Duration.ZERO, Duration.ofSeconds(3), Duration.ZERO)
        );
        player.showTitle(title);
    }

    private static final int BORDER_BLOCKS = 10; // minimum amount of blocks between the respawn position and the nearest border

    private void respawn(final @NotNull Location location) {
        player.teleport(location);
        Game.INVULNERABLE_DATA.write(player, true);
        player.setGameMode(GameMode.SURVIVAL);
        Bukkit.getScheduler().runTaskLater(
                GunGaming.plugin(),
                () -> {
                    if (!player.isValid()) return;
                    Game.INVULNERABLE_DATA.write(player, false);
                },
                GameConfig.RESPAWN_PROTECTION_SECONDS.get() * 20
        );
    }

    @Override
    protected void expire() {
        player.clearTitle();
        final GameTeam team = GameTeam.ofPlayer(player);
        if (team.size() > 1) {
            final Optional<Player> teammate = team.players().stream()
                    .filter(p -> p.getGameMode() != GameMode.SPECTATOR)
                    .filter(p -> !p.getUniqueId().equals(player.getUniqueId()))
                    .findFirst();
            if (teammate.isPresent()) {
                final Location location = teammate.get().getLocation();
                final World world = location.getWorld();
                final int x = location.getBlockX();
                final int z = location.getBlockZ();
                for (int y = location.getBlockY(); y > world.getMinHeight(); y--) {
                    final Block block = world.getBlockAt(x, y, z);
                    if (block.isEmpty()) continue;
                    final Block up1 = world.getBlockAt(x, y + 1, z);
                    final Block up2 = world.getBlockAt(x, y + 2, z);
                    if (!(up1.isEmpty() && up2.isEmpty())) break;
                    respawn(up1.getLocation().add(.5, 0, .5));
                    return;
                }
                final int y = world.getHighestBlockYAt(x, z) + 1;
                respawn(new Location(
                        world,
                        x + .5,
                        y,
                        z + .5
                ));
                return;
            }
        }
        final World world = player.getWorld();
        final int xMin = game.xMin() + BORDER_BLOCKS;
        final int zMin = game.zMin() + BORDER_BLOCKS;
        final int xMax = game.xMax() - BORDER_BLOCKS;
        final int zMax = game.zMax() - BORDER_BLOCKS;
        respawning:
        for (int i = 0; i < 1000; i++) {
            final int x = random.nextInt(Math.abs(xMax - xMin)) + xMin;
            final int z = random.nextInt(Math.abs(zMax - zMin)) + zMin;
            for (int y = world.getMaxHeight() - 3; y > world.getMinHeight(); y--) {
                final Block block = world.getBlockAt(x, y, z);
                if (block.isEmpty() || !block.getType().isOccluding()) continue;
                final Block up1 = world.getBlockAt(x, y + 1, z);
                final Block up2 = world.getBlockAt(x, y + 2, z);
                if (!(up1.isEmpty() && up2.isEmpty())) continue respawning;
                respawn(up1.getLocation().add(.5, 0, .5));
                return;
            }
        }
        new Respawn(game, player);
        throw new RuntimeException("Could not respawn player in 1000 attempts!");
    }
}
