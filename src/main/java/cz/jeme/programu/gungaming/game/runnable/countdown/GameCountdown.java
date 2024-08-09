package cz.jeme.programu.gungaming.game.runnable.countdown;

import cz.jeme.programu.gungaming.config.GameConfig;
import cz.jeme.programu.gungaming.game.Game;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class GameCountdown extends Countdown {

    private final @NotNull Game game;

    public GameCountdown(final @NotNull Game game) {
        super(GameConfig.GAME_SECONDS.get(), game.bossBar());
        this.game = game;
        game.bossBar().color(BossBar.Color.RED);
    }

    @Override
    protected void tick(final long counter, final float phase) {
        if ((counter < (60 * 5) && counter % 60 == 0) || counter % (60 * 5) == 0) {
            final String minutePlr = counter == 60 ? "minute" : "minutes";
            for (final Player player : Bukkit.getOnlinePlayers()) {
                player.sendRichMessage(
                        "<transition:#FF0000:#FFFF00:#00FF00:" + phase
                        + ">ℹ Game ends in " + counter / 60 + " " + minutePlr + "!"
                );
                player.playSound(Game.DING_SOUND, player);
            }
            return;
        }
        if (counter <= 10) {
            final String secondPlr = counter == 1 ? "second" : "seconds";
            for (final Player player : Bukkit.getOnlinePlayers()) {
                player.sendRichMessage(
                        "<transition:#FF0000:#FFFF00:#00FF00:" + phase
                        + ">ℹ Game ends in " + counter + " " + secondPlr + "!"
                );
                player.playSound(Game.DING_SOUND, player);
            }
        }
    }

    @Override
    protected void expire() {
        game.endGame();
    }
}
