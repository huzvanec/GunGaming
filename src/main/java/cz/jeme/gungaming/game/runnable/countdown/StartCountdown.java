package cz.jeme.gungaming.game.runnable.countdown;

import cz.jeme.gungaming.config.GameConfig;
import cz.jeme.gungaming.game.Game;
import cz.jeme.gungaming.util.Components;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;

@ApiStatus.Internal
@NullMarked
public final class StartCountdown extends Countdown {
    private final Game game;

    public StartCountdown(final Game game) {
        super(GameConfig.COUNTDOWN_SECONDS.get(), null);
        this.game = game;
    }

    @Override
    protected void tick(final long counter, final float phase) {
        final Title title = Title.title(
                Components.of("<transition:#FF0000:#FFFF00:#00FF00:" + phase + ">" + counter),
                Components.of("<gold><b>" + Components.latinString("Prepare for drop")),
                Title.Times.times(Duration.ZERO, Duration.ofSeconds(3), Duration.ZERO)
        );
        for (final Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(title);
            player.playSound(Game.DONG_SOUND, player);
        }
    }

    @Override
    protected void expire() {
        final Title title = Title.title(
                Components.of("<blue>Good Luck!"),
                Component.empty(),
                Title.Times.times(Duration.ZERO, Duration.ofMillis(500), Duration.ofSeconds(2))
        );
        for (final Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(title);
            player.playSound(Game.START_SOUND, player);
        }
        game.startGame();
    }
}
