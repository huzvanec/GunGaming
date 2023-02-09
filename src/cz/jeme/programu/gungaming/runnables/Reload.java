package cz.jeme.programu.gungaming.runnables;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import cz.jeme.programu.gungaming.managers.ReloadManager;
import cz.jeme.programu.gungaming.utils.AmmoLoreUtils;
import cz.jeme.programu.gungaming.utils.InventoryUtils;

public class Reload extends BukkitRunnable {

	private Material material;
	private ItemStack item;
	private Player player;
	private int ammoCount;
	private ReloadManager reloadManager;
	private ItemStack ammoItem;
	private boolean isCreative;

	public Reload(ItemStack item, Player player, ReloadManager reloadManager, int ammoCount, ItemStack ammoItem,
			boolean isCreative) {
		this.item = item;
		this.material = item.getType();
		this.reloadManager = reloadManager;
		this.player = player;
		this.ammoCount = ammoCount;
		this.ammoItem = ammoItem;
		this.isCreative = isCreative;
	}

	@Override
	public void run() {
		if (!isCreative) {
			InventoryUtils.removeItems(player.getInventory(), ammoItem, ammoCount);
		}
		AmmoLoreUtils.addAmmo(item, ammoCount);
		reloadManager.removeReload(player, material);
	}

}
