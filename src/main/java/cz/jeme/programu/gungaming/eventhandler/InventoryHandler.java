package cz.jeme.programu.gungaming.eventhandler;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.attachment.AttachmentMenu;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.manager.ReloadManager;
import cz.jeme.programu.gungaming.manager.ZoomManager;
import cz.jeme.programu.gungaming.util.Sounds;
import cz.jeme.programu.gungaming.util.registry.Guns;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class InventoryHandler {
    private static final @NotNull Map<UUID, AttachmentMenu> ATTACHMENT_MENUS = new HashMap<>();
    private InventoryHandler() {
        throw new AssertionError();
    }

    public static void onPlayerSwapHands(@NotNull PlayerSwapHandItemsEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (!Guns.isGun(item)) {
            return;
        }
        event.setCancelled(true);
        ReloadManager.INSTANCE.reload(event.getPlayer(), item);
    }

    public static void onInventoryOpen(@NotNull InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            throw new IllegalArgumentException("HumanEntity is not instanceof Player!");
        }
        ReloadManager.INSTANCE.abortReloads((Player) event.getPlayer());
    }

    public static void onPlayerDropItem(@NotNull PlayerDropItemEvent event) {
        ReloadManager.INSTANCE.abortReloads(event.getPlayer());
        ZoomManager.INSTANCE.zoomOut(event.getPlayer());
    }

    public static void onInventoryClick(@NotNull InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            throw new IllegalArgumentException("HumanEntity is not instanceof Player!");
        }
        ReloadManager.INSTANCE.abortReloads(player);
        ZoomManager.INSTANCE.zoomOut(player);


        ItemStack cursor = event.getCursor();
        ItemStack item = event.getCurrentItem();

        if (event.getInventory().getType() == InventoryType.ANVIL) {
            if (item == null) return;
            if (item.getItemMeta() == null) return;
            if (Namespace.GG.has(item)) {
                event.setCancelled(true);
                return;
            }
        }

        boolean rightClick = event.getClick() == ClickType.RIGHT;
        boolean emptyClick = cursor.getType() == Material.AIR;
        boolean isGun = Guns.isGun(item);

        if (rightClick && emptyClick && isGun) {
            event.setCancelled(true);
            ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
            serverPlayer.inventoryMenu.sendAllDataToRemote();
            ATTACHMENT_MENUS.put(player.getUniqueId(), new AttachmentMenu(event));
            return;
        }
        AttachmentMenu menu = ATTACHMENT_MENUS.get(player.getUniqueId());
        if (menu == null) return;
        if (menu.hasOpenInventory(event)) {
            menu.click(event);
        } else {
            ATTACHMENT_MENUS.put(player.getUniqueId(), null);
        }
    }

    public static void onHotbarSlotSwitch(@NotNull PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ZoomManager.INSTANCE.zoomOut(player);
        ReloadManager.INSTANCE.abortReloads(player);
        ItemStack heldItem = player.getInventory().getItem(event.getNewSlot());
        if (Guns.isGun(heldItem)) {
            Gun gun = Guns.getGun(heldItem);
            player.playSound(Sounds.getGunSwitchSound(gun), player);
        }
    }

    public static void onInventoryClose(@NotNull InventoryCloseEvent event) {
        // When the AttachmentMenu is closed, it doesn't start remote updates to the player's inventory
        // This leads to client-server desync. This starts the updates again.
        // Took like 3 days to discover.
        ServerPlayer serverPlayer = ((CraftPlayer) event.getPlayer()).getHandle();
        serverPlayer.inventoryMenu.resumeRemoteUpdates();
    }
}
