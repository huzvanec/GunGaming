package cz.jeme.programu.gungaming.game.runnable;

import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApiStatus.Internal
public abstract class GameRunnable extends BukkitRunnable {
    private static final @NotNull Queue<GameRunnable> RUNNABLES = new ConcurrentLinkedQueue<>();

    public static void cancelAll() {
        RUNNABLES.forEach(GameRunnable::cancel);
    }

    public GameRunnable() {
        RUNNABLES.add(this);
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        RUNNABLES.remove(this);
        super.cancel();
    }
}