package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.util.Messages;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.List;

public final class Game {
    private static @Nullable Game game;
    private final int size;
    private final int centerX;
    private final int centerZ;
    private final @NotNull CommandSender sender;
    private final @NotNull List<Player> players;

    private Game(int size, int centerX, int centerZ, @NotNull CommandSender sender) {
        this.sender = sender;
        this.size = size;
        this.centerX = centerX;
        this.centerZ = centerZ;

        final World overworld = Bukkit.getWorlds().stream()
                .filter(w -> w.getEnvironment() == World.Environment.NORMAL)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No overworld found!"));

        players = List.copyOf(Bukkit.getOnlinePlayers());


        for (Player player : players) {
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(new Location(overworld, centerX, 330, centerZ));
            Namespace.FROZEN.set(player, true);
        }
    }

    public static boolean isRunning() {
        return game != null;
    }

    public static @Nullable Game newInstance(int size, int centerX, int centerZ, @NotNull CommandSender sender) {
        if (isRunning()) return null;
        game = new Game(size, centerX, centerZ, sender);
        return game;
    }

    private class StartCountdown extends BukkitRunnable {
        private final int counterStart = 10;
        private int counter = counterStart;

        private StartCountdown() {
            runTaskTimer(GunGaming.getPlugin(), 0L, 20L);
        }

        @Override
        public void run() {
            if (counter == 0) {
                cancel();
                return;
            }
            Title title = Title.title(
                    Messages.from("<transition:#00FF00:#FF0000:"
                            + counterStart / (float) counter + ">"
                            + counter + "</transition>"),
                    Messages.from("<gold>Game is starting...</gold>"),
                    Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO)
            );
            for (Player player : players) {
                player.showTitle(title);
//                player.playSound();
            }
            counter--;
        }
    }
}
