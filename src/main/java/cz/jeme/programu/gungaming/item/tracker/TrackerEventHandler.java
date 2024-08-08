package cz.jeme.programu.gungaming.item.tracker;

import cz.jeme.programu.gungaming.config.GameConfig;
import cz.jeme.programu.gungaming.game.Game;
import cz.jeme.programu.gungaming.item.CustomItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public final class TrackerEventHandler {
    private TrackerEventHandler() {
        throw new AssertionError();
    }

    public static void onInventoryClick(final @NotNull InventoryClickEvent event) {
        if (!Game.running()) return;
        if (GameConfig.TEAM_PLAYERS.get() == 1) return;
        if (event.getSlot() != 8) return;
        if (!(event.getClickedInventory() instanceof PlayerInventory)) return;
        if (!CustomItem.is(event.getCurrentItem(), TeammateTracker.class)) return;
        event.setCancelled(true);
    }

    public static void onPlayerDropItem(final @NotNull PlayerDropItemEvent event) {
        if (!Game.running()) return;
        if (GameConfig.TEAM_PLAYERS.get() == 1) return;
        if (event.getPlayer().getInventory().getHeldItemSlot() != 8) return;
        event.setCancelled(true);
    }
}
