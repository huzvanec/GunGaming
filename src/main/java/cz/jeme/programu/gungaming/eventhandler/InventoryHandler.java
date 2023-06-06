package cz.jeme.programu.gungaming.eventhandler;

import cz.jeme.programu.gungaming.manager.ReloadManager;
import cz.jeme.programu.gungaming.manager.ZoomManager;
import cz.jeme.programu.gungaming.util.item.Guns;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryHandler {

    private final ReloadManager reloadManager;
    private final ZoomManager zoomManager;

    public InventoryHandler(ReloadManager reloadManager, ZoomManager zoomManager) {
        this.reloadManager = reloadManager;
        this.zoomManager = zoomManager;
    }

    public void onPlayerSwapHands(PlayerSwapHandItemsEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (!Guns.isGun(item)) {
            return;
        }
        event.setCancelled(true);
        reloadManager.reload(event.getPlayer(), item);
    }

    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            throw new IllegalArgumentException("HumanEntity is not instanceof Player!");
        }
        reloadManager.abortReloads((Player) event.getPlayer());
    }
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        reloadManager.abortReloads(event.getPlayer());
        zoomManager.zoomOut(event.getPlayer());
    }

    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            throw new IllegalArgumentException("HumanEntity is not instanceof Player!");
        }
        reloadManager.abortReloads(player);
        zoomManager.zoomOut(player);
    }
}
