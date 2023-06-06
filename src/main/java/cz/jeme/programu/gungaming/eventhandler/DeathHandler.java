package cz.jeme.programu.gungaming.eventhandler;

import cz.jeme.programu.gungaming.managers.ReloadManager;
import cz.jeme.programu.gungaming.managers.ZoomManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathHandler {
    ReloadManager reloadManager;
    ZoomManager zoomManager;

    public DeathHandler(ReloadManager reloadManager, ZoomManager zoomManager) {
        this.reloadManager = reloadManager;
        this.zoomManager = zoomManager;
    }

    public void onPlayerDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();
        zoomManager.zoomOut(dead);
        reloadManager.abortReloads(dead, false);
        Player killer = dead.getKiller();
        dead.getWorld().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        dead.setGameMode(GameMode.SPECTATOR);
        if (killer == null) {
            event.setDeathMessage(ChatColor.RED + event.getDeathMessage());
            return;
        }
        if (dead.getUniqueId().equals(killer.getUniqueId())) {
            event.setDeathMessage(ChatColor.RED + dead.getName() + " committed a suicide");
            return;
        }
        event.setDeathMessage(ChatColor.DARK_RED + dead.getName() + " was killed by " + killer.getName());
    }
}
