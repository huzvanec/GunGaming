package cz.jeme.programu.gungaming.eventhandlers;

import cz.jeme.programu.gungaming.managers.ReloadManager;
import cz.jeme.programu.gungaming.utils.GunUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryHandler {

    private final ReloadManager reloadManager;

    public InventoryHandler(ReloadManager reloadManager) {
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

    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            throw new IllegalArgumentException("HumanEntity is not instanceof Player!");
        }
        reloadManager.abortReloads((Player) event.getPlayer());
    }

    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            throw new IllegalArgumentException("HumanEntity is not instanceof Player!");
        }
        reloadManager.abortReloads((Player) event.getWhoClicked());
    }
}
