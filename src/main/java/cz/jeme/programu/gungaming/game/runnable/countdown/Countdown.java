package cz.jeme.programu.gungaming.game.runnable.countdown;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.game.runnable.GameRunnable;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.bossbar.BossBar;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

@ApiStatus.Internal
public abstract class Countdown extends GameRunnable {
    private static final @NotNull DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("00");
    private final long duration;
    private final @Nullable BossBar bossBar;
    private long counter;

    public Countdown(final long duration, @Nullable final BossBar bossBar) {
        this.bossBar = bossBar;
        this.duration = duration;
        counter = duration;
        runTaskTimer(GunGaming.plugin(), 0L, 20L);
    }

    @Override
    public final void run() {
        if (counter == 0) {
            expire();
            cancel();
            return;
        }
        final float phase = (float) counter / duration;
        if (bossBar != null) {
            bossBar.progress(phase);
            bossBar.name(Components.of("<b><transition:#FF0000:#FFFF00:#00FF00:" + phase + ">"
                                       + translateTime(counter)
            ));
        }
        tick(counter, phase);
        counter--;
    }

    protected void expire() {
    }

    protected void tick(final long counter, final float phase) {
    }

    private static @NotNull String translateTime(final long totalSeconds) {
        final long hours = totalSeconds / 3600;
        final long minutes = totalSeconds % 3600 / 60;
        final long seconds = totalSeconds % 60;
        final String hoursStr = hours == 0 ? "" : DECIMAL_FORMATTER.format(hours) + ":";
        return hoursStr + DECIMAL_FORMATTER.format(minutes) + ":" + DECIMAL_FORMATTER.format(seconds);
    }
}
