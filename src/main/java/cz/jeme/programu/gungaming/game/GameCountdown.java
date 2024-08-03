package cz.jeme.programu.gungaming.game;

import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class GameCountdown extends Countdown {

    private final @NotNull Game game;

    public GameCountdown(final @NotNull Game game, final int duration) {
//        super(duration, game.bossBar());
        super(15, game.bossBar());
        this.game = game;
        game.bossBar().color(BossBar.Color.RED);
    }

    @Override
    protected void tick(final long counter, final float phase) {
        if ((counter < (60 * 5) && counter % 60 == 0) || counter % (60 * 5) == 0) {
            final String minutePlr = counter == 60 ? "minute" : "minutes";
            for (final Player player : game.players) {
                player.sendMessage(Components.of(
                        "<transition:#FF0000:#FFFF00:#00FF00:" + phase
                        + ">ℹ Game ends in " + counter / 60 + " " + minutePlr + "!"
                ));
                player.playSound(Game.DING_SOUND, player);
            }
            return;
        }
        if (counter <= 10) {
            final String secondPlr = counter == 1 ? "second" : "seconds";
            for (final Player player : game.players) {
                player.sendMessage(Components.of(
                        "<transition:#FF0000:#FFFF00:#00FF00:" + phase
                        + ">ℹ Game ends in " + counter + " " + secondPlr + "!"
                ));
                player.playSound(Game.DING_SOUND, player);
            }
        }
    }

    @Override
    protected void expire() {
        game.endGame();
    }
}
