package cz.jeme.programu.gungaming.eventhandler;

import cz.jeme.programu.gungaming.manager.ReloadManager;
import cz.jeme.programu.gungaming.manager.ZoomManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class HotbarSlotSwitchHandler {

    private final ReloadManager reloadManager;
    private final ZoomManager zoomManager;

    public HotbarSlotSwitchHandler(ZoomManager zoomManager, ReloadManager reloadManager) {
        this.zoomManager = zoomManager;
        this.reloadManager = reloadManager;
    }

    public void onHotbarSlotSwitch(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        zoomManager.zoomOut(player);
        reloadManager.abortReloads(player);
    }

}
