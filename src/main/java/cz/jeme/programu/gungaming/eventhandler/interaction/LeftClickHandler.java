package cz.jeme.programu.gungaming.eventhandler.interaction;

import cz.jeme.programu.gungaming.managers.ZoomManager;
import cz.jeme.programu.gungaming.utils.GunUtils;
import cz.jeme.programu.gungaming.utils.LoreUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class LeftClickHandler {
    private final ZoomManager zoomManager;

    public LeftClickHandler(ZoomManager zoomManager) {
        this.zoomManager = zoomManager;
    }

    public void air(PlayerInteractEvent event) {
        zoom(event);
    }

    public void block(PlayerInteractEvent event) {
        zoom(event);
    }

    private void zoom(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!GunUtils.isGun(item)) {
            return;
        }
        event.setCancelled(true);
        String zoomMultiplier = LoreUtils.getLore(item).get("Scope");
        if (zoomMultiplier != null) {
            zoomManager.nextZoom(player, Double.parseDouble(zoomMultiplier));
        }
    }
}
