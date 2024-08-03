package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.item.gun.Gun;
import net.minecraft.world.inventory.InventoryMenu;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public final class AttachmentEventHandler {
    private AttachmentEventHandler() {
        throw new AssertionError();
    }

    public static void onInventoryClick(final @NotNull InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof final Player player)) return;
        ZoomManager.INSTANCE.zoomOut(player);
        final ItemStack item = event.getCurrentItem();
        final boolean rightClick = event.getClick() == ClickType.RIGHT;
        final boolean emptyHand = event.getCursor().isEmpty();
        final boolean playerInventory = event.getClickedInventory() instanceof PlayerInventory;
        final boolean isGun = Gun.is(item);
        if (rightClick && emptyHand && playerInventory && isGun) {
            event.setCancelled(true);
            AttachmentMenuManager.INSTANCE.createMenu(player, item);
            return;
        }

        final AttachmentMenu menu = AttachmentMenuManager.INSTANCE.getMenu(player);
        if (menu != null && event.getInventory() == menu.inventory()) {
            menu.inventoryClick(event);
            return;
        }
    }

    public static void onInventoryClose(final @NotNull InventoryCloseEvent event) {
        final HumanEntity player = event.getPlayer();
        final AttachmentMenu menu = AttachmentMenuManager.INSTANCE.getMenu(player);
        if (menu == null) return;
        if (event.getInventory() != menu.inventory()) return;
        // fix client-server desync after closing the attachment menu
        final InventoryMenu inventoryMenu = ((CraftHumanEntity) player).getHandle().inventoryMenu;
        inventoryMenu.resumeRemoteUpdates();
        inventoryMenu.sendAllDataToRemote();
        // remove attachment menu
        AttachmentMenuManager.INSTANCE.removeMenu(player);
    }

    public static void onPlayerDropItem(final @NotNull PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        ZoomManager.INSTANCE.zoomOut(player);
        final AttachmentMenu menu = AttachmentMenuManager.INSTANCE.getMenu(player);
        if (menu == null) return;
        menu.playerDropItem(event);
    }

    public static void onPlayerQuit(final @NotNull PlayerQuitEvent event) {
        ZoomManager.INSTANCE.zoomOut(event.getPlayer());
    }

    public static void onPlayerGameModeChange(final @NotNull PlayerGameModeChangeEvent event) {
        ZoomManager.INSTANCE.zoomOut(event.getPlayer());
    }

    public static void onPlayerDeath(final @NotNull PlayerDeathEvent event) {
        ZoomManager.INSTANCE.zoomOut(event.getEntity());
    }

    public static void onPlayerItemHeld(final @NotNull PlayerItemHeldEvent event) {
        ZoomManager.INSTANCE.zoomOut(event.getPlayer());
    }
}
