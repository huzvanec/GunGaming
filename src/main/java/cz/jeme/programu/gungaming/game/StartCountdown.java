package cz.jeme.programu.gungaming.game;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

final class StartCountdown extends Countdown {
    public static final @NotNull Sound START_SOUND = Sound.sound(GunGaming.namespaced("game.start"), Sound.Source.MASTER, 1, 1);
    private static final int DURATION = 30; // seconds

    private final @NotNull Game game;

    public StartCountdown(final @NotNull Game game) {
        super(DURATION, null);
        this.game = game;
    }

    @Override
    protected void tick(final long counter, final float phase) {
        final Title title = Title.title(
                Components.of("<transition:#FF0000:#FFFF00:#00FF00:" + phase + ">" + counter),
                Components.of("<gold><b>" + Components.latinString("The game will start soon")),
                Title.Times.times(Duration.ZERO, Duration.ofSeconds(3), Duration.ZERO)
        );
        for (final Player player : game.players) {
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
        for (final Player player : game.players) {
            player.showTitle(title);
            player.playSound(START_SOUND, player);
        }
        game.startGame();
    }
}
