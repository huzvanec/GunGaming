package cz.jeme.programu.gungaming.eventhandler.interaction;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.attachment.scope.Scope;
import cz.jeme.programu.gungaming.manager.ZoomManager;
import cz.jeme.programu.gungaming.util.registry.Attachments;
import cz.jeme.programu.gungaming.util.registry.Guns;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class LeftClickHandler {
    private LeftClickHandler() {
        throw new AssertionError();
    }

    public static void air(@NotNull PlayerInteractEvent event) {
        zoom(event);
    }

    public static void block(@NotNull PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        if (Attachments.isAttachment(item)) {
            event.setCancelled(true);
        } else if (Guns.isGun(item) && player.isSneaking()) {
            zoom(event);
            event.setCancelled(true);
        }
    }

    private static void zoom(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!Guns.isGun(item)) return;
        String scopeName = Namespace.GUN_SCOPE.get(item);
        assert scopeName != null : "Scope name is null!";
        Scope scope = (Scope) Attachments.getAttachment(scopeName);
        if (scope == null) return;
        ZoomManager.INSTANCE.nextZoom(player, scope.scope);
    }
}