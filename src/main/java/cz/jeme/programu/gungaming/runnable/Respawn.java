package cz.jeme.programu.gungaming.runnable;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.game.Game;
import cz.jeme.programu.gungaming.util.Message;
import cz.jeme.programu.gungaming.util.Sounds;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Random;

public final class Respawn extends BukkitRunnable {

    private final @NotNull Player player;
    private static final @NotNull Random RANDOM = new Random();
    private static final int RESPAWN_TIME = 20; // Respawn time in seconds
    private int counter = RESPAWN_TIME;

    public Respawn(@NotNull Player player) {
        this.player = player;
        player.setGameMode(GameMode.SPECTATOR);
        runTaskTimer(GunGaming.getPlugin(), 0L, 20L);
    }

    @Override
    public void run() {
        Title title;
        if (counter == 0) {
            player.clearTitle();
            Game game = Game.getInstance();
            assert game != null : "Tried to respawn with game null!";
            int x = game.getCenterX() + rel();
            int z = game.getCenterZ() + rel();
            int y = Game.getWorld().getHighestBlockYAt(x, z) + 1;
            player.teleport(player.getLocation().set(x + 0.5, y, z + 0.5));
            player.setGameMode(GameMode.SURVIVAL);
            title = Title.title(
                    Message.from("<aqua>Good Luck!</aqua>"),
                    Message.EMPTY,
                    Title.Times.times(Duration.ZERO, Duration.ofMillis(300), Duration.ofSeconds(1))
            );
            Sounds.dong(player);
            cancel();
        } else {
            final float phase = (float) (counter - 1) / RESPAWN_TIME;
            title = Title.title(
                    Message.from("<transition:#FF0000:#00FF00:" + phase + ">" + counter + "</transition>"),
                    Message.from(" <gold>Respawning...</gold>"),
                    Title.Times.times(Duration.ZERO, Duration.ofSeconds(5), Duration.ZERO)
            );
        }
        player.showTitle(title);
        counter--;
    }

    private static int rel() {
        Game game = Game.getInstance();
        assert game != null : "Tried to respawn with game null!";
        return Math.round(RANDOM.nextInt(game.getSize()) - game.getSize() / 2f);
    }
}
