package cz.jeme.programu.gungaming.eventhandlers;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;

import cz.jeme.programu.gungaming.managers.ReloadManager;
import cz.jeme.programu.gungaming.managers.ZoomManager;

public class HotbarSlotSwichHandler {

	private ReloadManager reloadManager;
	private ZoomManager zoomManager;

	public HotbarSlotSwichHandler(ZoomManager zoomManager, ReloadManager reloadManager) {
		this.zoomManager = zoomManager;
		this.reloadManager = reloadManager;
	}

	public void onHotbarSlotSwich(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		zoomManager.zoomOut(player);
		reloadManager.abortReloads(player);
	}

}
