package cz.jeme.programu.gungaming.eventhandler.interaction;

import cz.jeme.programu.gungaming.item.attachment.scope.Scope;
import cz.jeme.programu.gungaming.manager.ZoomManager;
import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.util.item.Attachments;
import cz.jeme.programu.gungaming.util.item.Guns;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class LeftClickHandler {
    private final ZoomManager zoomManager = ZoomManager.getInstance();

    public void air(PlayerInteractEvent event) {
        zoom(event);
    }

    public void block(PlayerInteractEvent event) {
        zoom(event);
    }

    private void zoom(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!Guns.isGun(item)) {
            if (Attachments.isAttachment(item)) {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        String scopeName = Namespace.GUN_SCOPE.get(item);
        assert scopeName != null : "Scope name is null!";
        Scope scope = (Scope) Attachments.getAttachment(scopeName);
        assert scope != null : "Scope is null!";
        zoomManager.nextZoom(player, scope.scope);
    }
}
