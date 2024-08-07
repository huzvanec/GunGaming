package cz.jeme.programu.gungaming.game;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.config.GameConfig;
import cz.jeme.programu.gungaming.loot.crate.CrateGenerator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

final class RefillRunnable extends BukkitRunnable {
    public RefillRunnable() {
        final int duration = GameConfig.REFILL_SECONDS.get() * 20;
        runTaskTimer(GunGaming.plugin(), duration, duration);
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
