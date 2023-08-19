package cz.jeme.programu.gungaming.eventhandler;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.manager.ReloadManager;
import cz.jeme.programu.gungaming.manager.ZoomManager;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.Packets;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public class DeathHandler {
    private final @NotNull ReloadManager reloadManager = ReloadManager.getInstance();
    private final @NotNull ZoomManager zoomManager = ZoomManager.getInstance();

    public void onPlayerDeath(@NotNull PlayerDeathEvent event) {
        Player dead = event.getEntity();
        zoomManager.zoomOut(dead);
        reloadManager.abortReloads(dead, false);
        Player killer = dead.getKiller();
        dead.setGameMode(GameMode.SPECTATOR);

        Packets.sendPacket(dead, new ClientboundGameEventPacket(
                ClientboundGameEventPacket.IMMEDIATE_RESPAWN,
                1F
        ));

        Bukkit.getScheduler().runTaskLater(GunGaming.getPlugin(), () -> {
            dead.spigot().respawn();
            dead.teleport(new Location(dead.getWorld(), 0, 100, 0));
        }, 1L);

        Component deathMessage = event.deathMessage();
        if (deathMessage == null) return;

        if (killer == null || dead.getUniqueId().equals(killer.getUniqueId())) {
            event.deathMessage(Messages.from("<red>" + Messages.to(deathMessage) + "</red>"));
            return;
        }
        event.deathMessage(Messages.from("<dark_red>" + Messages.to(deathMessage) + "</dark_red>"));
    }
}
