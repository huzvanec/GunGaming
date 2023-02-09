package cz.jeme.programu.gungaming.eventhandlers;

import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import cz.jeme.programu.gungaming.managers.ReloadManager;
import cz.jeme.programu.gungaming.utils.GunUtils;

public class SwapHandsHandler {

	private ReloadManager reloadManager;

	public SwapHandsHandler(ReloadManager reloadManager) {
		this.reloadManager = reloadManager;
	}

	public void onSwapHands(PlayerSwapHandItemsEvent event) {
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if (!GunUtils.isGun(item)) {
			return;
		}
		event.setCancelled(true);
		reloadManager.reload(event.getPlayer(), item);
	}
}
