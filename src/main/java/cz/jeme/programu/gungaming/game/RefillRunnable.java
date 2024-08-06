package cz.jeme.programu.gungaming.game;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.loot.crate.CrateGenerator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

final class RefillRunnable extends BukkitRunnable {
    private static final int DURATION = 6 * 60 * 20; // 6 minutes

    public RefillRunnable() {
        runTaskTimer(GunGaming.plugin(), DURATION, DURATION);
    }

    @Override
    public void run() {
        CrateGenerator.INSTANCE.refillCrates(null);
        for (final Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(Game.WARNING_SOUND, player);
            player.sendRichMessage("<#00FFFF>ℹ All crates were refilled!");
        }
    }
}
