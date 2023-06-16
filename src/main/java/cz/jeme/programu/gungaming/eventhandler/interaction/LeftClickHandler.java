package cz.jeme.programu.gungaming.eventhandler.interaction;

import cz.jeme.programu.gungaming.item.attachment.scope.Scope;
import cz.jeme.programu.gungaming.manager.ZoomManager;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.util.item.Attachments;
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

        if (!Guns.isGun(item)) {
            if (Attachments.isAttachment(item)) {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        Scope scope = (Scope) Attachments.getAttachment((String) Namespaces.GUN_SCOPE.get(item));
        if (scope == null) {
            player.sendActionBar(Messages.from("<red>This gun has no scope equipped!</red>"));
            return;
        }
        zoomManager.nextZoom(player, scope.scope);
    }
}
