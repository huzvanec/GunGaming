package cz.jeme.gungaming.game.runnable;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.game.Game;
import cz.jeme.gungaming.loot.crate.CrateGenerator;
import cz.jeme.gungaming.util.Components;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;

@ApiStatus.Internal
@NullMarked
public final class Loading extends GameRunnable {
    private int dotsCount = 1;

    private final Game game;

    public Loading(final Game game) {
        this.game = game;
        runTaskTimer(GunGaming.instance(), 0L, 20L);
    }

    @Override
    public void run() {
        if (!CrateGenerator.INSTANCE.generating()) {
            Bukkit.getOnlinePlayers().forEach(Player::clearTitle);
            game.loadingEnd();
            cancel();
            return;
        }
        if (dotsCount >= 3) dotsCount = 1;
        else dotsCount++;

        final Title title = Title.title(
                Components.of("<blue>Loading..."),
                Components.of("<gold>" + Components.latinString("Generating loot") + ".".repeat(dotsCount)),
                Title.Times.times(Duration.ZERO, Duration.ofSeconds(30), Duration.ZERO)
        );
        Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(title));
    }
}
