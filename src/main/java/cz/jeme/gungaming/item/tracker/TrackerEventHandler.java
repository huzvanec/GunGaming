package cz.jeme.gungaming.item.tracker;

import cz.jeme.gungaming.game.Game;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.item.tracker.impl.TeammateTracker;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.PlayerInventory;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TrackerEventHandler {
    private TrackerEventHandler() {
        throw new AssertionError();
    }

    public static void onInventoryClick(final InventoryClickEvent event) {
        if (!Game.running()) return;
        if (event.getSlot() != Game.TEAM_COMPASS_SLOT) return;
        if (!(event.getClickedInventory() instanceof PlayerInventory)) return;
        if (!CustomItem.is(event.getCurrentItem(), TeammateTracker.class)) return;
        event.setCancelled(true);
    }

    public static void onPlayerDropItem(final PlayerDropItemEvent event) {
        if (!Game.running()) return;
        if (event.getPlayer().getInventory().getHeldItemSlot() != Game.TEAM_COMPASS_SLOT) return;
        event.setCancelled(true);
    }
}
