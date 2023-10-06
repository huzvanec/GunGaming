package cz.jeme.programu.gungaming.game;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.util.Message;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public abstract class Timer extends BukkitRunnable {
    private static final DecimalFormat FORMATTER = new DecimalFormat("00");
    private final long duration;
    private final @Nullable BossBar bossBar;
    private long counter;

    public Timer(long duration, @Nullable BossBar bossBar) {
        this.bossBar = bossBar;
        this.duration = duration;
        counter = duration;
        runTaskTimer(GunGaming.getPlugin(), 0L, 20L);
    }

    @Override
    public final void run() {
        if (counter == 0) {
            cancel();
            expire();
            return;
        }
        if (bossBar != null) {
            float phase = (float) counter / duration;
            bossBar.progress(phase);
            bossBar.name(Message.from("<b><transition:#FF0000:#00FF00:" + phase + ">"
                    + translateTime(counter) + "</transition></b>"
            ));
        }
        counter--;
    }

    abstract protected void expire();

    private static String translateTime(long seconds) {
        long hours = seconds / 3600;
        seconds -= hours * 3600;
        long minutes = seconds / 60;
        seconds -= minutes * 60;
        String time = FORMATTER.format(minutes) + ":" + FORMATTER.format(seconds);
        if (hours != 0) {
            time = FORMATTER.format(hours) + ":" + time;
        }
        return time;
    }
}
