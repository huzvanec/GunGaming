package cz.jeme.gungaming.game.lobby;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.util.Components;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class LobbyRunnable extends BukkitRunnable {
    private static final long PERIOD_TICKS = 4L;

    private int counter = 0;

    public LobbyRunnable() {
        runTaskTimer(GunGaming.instance(), 0L, PERIOD_TICKS);
    }

    @Override
    public void run() {
        final Component message = Components.of(message());
        Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar(message));
        counter++;
    }

    private static final String GUN_GAMING_TEXT = "GunGaming v" + GunGaming.instance().getPluginMeta().getVersion();
    private static final int DOT_REPETITIONS = 30;
    private static final int VERSION_FLASH_REPETITIONS = 8;

    private String message() {
        if (counter - DOT_REPETITIONS * 3 - VERSION_FLASH_REPETITIONS >= GUN_GAMING_TEXT.length()) counter = 0;
        if (counter <= DOT_REPETITIONS * 3)
            return "<aqua>" + Components.latinString("Waiting for players") + ".".repeat(counter % 3 + 1);
        if (counter <= DOT_REPETITIONS * 3 + GUN_GAMING_TEXT.length()-1)
            return "<blue><b>" + GUN_GAMING_TEXT.substring(0, counter - (DOT_REPETITIONS * 3 - 1));
        final boolean isBlue = counter % 2 == 1;
        return (isBlue ? "<blue>" : "<gold>") + "<b>" + GUN_GAMING_TEXT;
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar(Component.empty()));
        super.cancel();
    }
}
