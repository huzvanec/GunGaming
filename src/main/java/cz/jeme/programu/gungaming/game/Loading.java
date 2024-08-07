package cz.jeme.programu.gungaming.game;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.loot.crate.CrateGenerator;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

final class Loading extends BukkitRunnable {
    private int dotsCount = 1;

    private final @NotNull Game game;

    public Loading(final @NotNull Game game) {
        this.game = game;
        runTaskTimer(GunGaming.plugin(), 0L, 20L);
    }

    @Override
    public void run() {
        if (!CrateGenerator.INSTANCE.generating()) {
            for (final Player player : Bukkit.getOnlinePlayers()) player.clearTitle();
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
