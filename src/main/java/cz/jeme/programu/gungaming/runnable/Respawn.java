package cz.jeme.programu.gungaming.runnable;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.util.Message;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public final class Respawn extends BukkitRunnable {

    private final @NotNull Player player;
    private static final int COUNTER_START = 10;
    private int counter = COUNTER_START;

    public Respawn(@NotNull Player player) {
        this.player = player;
        player.setGameMode(GameMode.SPECTATOR);
        runTaskTimer(GunGaming.getPlugin(), 0L, 20L);
    }

    @Override
    public void run() {
        if (counter == -1) {
            player.clearTitle();
            player.setGameMode(GameMode.SURVIVAL);
            cancel();
            return;
        }
        final float phase = (float) counter / COUNTER_START;
        Title.Times times = Title.Times.times(Duration.ZERO, Duration.ofSeconds(5), Duration.ZERO);
        Title title = Title.title(
                Message.from("<transition:#FF0000:#00FF00:" + phase + ">" + counter + "</transition>"),
                Message.from("Respawning..."),
                times
        );
        player.showTitle(title);
        counter--;
    }
}
