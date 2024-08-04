package cz.jeme.programu.gungaming.game;

import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class GracePeriodCountdown extends Countdown {

    private static final int DURATION = 3 * 60 + 30; // seconds = 3 minutes and 30 seconds (extra 30 for the glide down)

    private final @NotNull Game game;

    public GracePeriodCountdown(final @NotNull Game game) {
        super(DURATION, game.bossBar());
        this.game = game;
        game.bossBar().color(BossBar.Color.RED);
    }

    @Override
    protected void tick(final long counter, final float phase) {
        if (counter % 60 == 0) {
            final String minutePlr = counter == 60 ? "minute" : "minutes";
            for (final Player player : game.players) {
                player.sendMessage(Components.of(
                        "<transition:#FF0000:#FFFF00:#00FF00:" + phase + ">ℹ "
                        + counter / 60 + " " + minutePlr
                        + " of grace period remaining!"
                ));
                player.playSound(Game.DING_SOUND, player);
            }
            return;
        }
        if (counter == 10) {
            for (final Player player : game.players) {
                player.sendMessage(Components.of("<transition:#FF0000:#FFFF00:#00FF00:" + phase +
                                                 ">ℹ 10 seconds of grace period remaining!"));
                player.playSound(Game.DING_SOUND, player);
            }
        }
    }

    @Override
    protected void expire() {
        for (final Player player : game.players) {
            player.sendMessage(Components.of("<#FF0000>ℹ The grace period is over! PvP is now enabled."));
            player.playSound(Game.DING_SOUND, player);
        }
        game.gracePeriodEnd();
    }
}
