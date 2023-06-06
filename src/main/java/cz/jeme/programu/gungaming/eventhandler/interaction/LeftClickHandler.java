package cz.jeme.programu.gungaming.eventhandler.interaction;

import cz.jeme.programu.gungaming.manager.ZoomManager;
import cz.jeme.programu.gungaming.util.Namespaces;
import cz.jeme.programu.gungaming.util.item.Guns;
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
        if (!Guns.isGun(item)) return;
        event.setCancelled(true);
        zoomManager.nextZoom(player, Namespaces.GUN_SCOPE.get(item));
    }
}
