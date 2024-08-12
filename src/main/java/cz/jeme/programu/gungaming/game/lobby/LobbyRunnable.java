package cz.jeme.programu.gungaming.game.lobby;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

final class LobbyRunnable extends BukkitRunnable {
    private int counter = 0;

    public LobbyRunnable() {
        runTaskTimer(GunGaming.plugin(), 0L, 4L);
    }

    @Override
    public void run() {
        final Component message = Components.of(message());
        Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar(message));
        counter++;
    }

    @SuppressWarnings("UnstableApiUsage")
    private static final @NotNull String GUN_GAMING_TEXT = "GunGaming v" + GunGaming.plugin().getPluginMeta().getVersion();
    private static final int DOT_REPETITIONS = 30;

    private @NotNull String message() {
        if (counter - DOT_REPETITIONS * 3 >= GUN_GAMING_TEXT.length()) counter = 0;
        if (counter <= DOT_REPETITIONS * 3)
            return "<aqua>" + Components.latinString("Waiting for players") + ".".repeat(counter % 3 + 1);
        return "<blue><b>" + GUN_GAMING_TEXT.substring(0, counter - (DOT_REPETITIONS * 3 - 1));
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar(Component.empty()));
        super.cancel();
    }
}
