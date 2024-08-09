package cz.jeme.programu.gungaming.game.runnable;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.config.GameConfig;
import cz.jeme.programu.gungaming.game.Game;
import cz.jeme.programu.gungaming.loot.crate.CrateGenerator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class RefillRunnable extends GameRunnable {
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