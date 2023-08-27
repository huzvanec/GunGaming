package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.eventhandler.*;
import cz.jeme.programu.gungaming.eventhandler.interaction.PlayerInteractHandler;
import cz.jeme.programu.gungaming.item.misc.GraplingHook;
import cz.jeme.programu.gungaming.manager.ZoomManager;
import cz.jeme.programu.gungaming.util.item.Attachments;
import cz.jeme.programu.gungaming.util.item.Guns;
import cz.jeme.programu.gungaming.util.item.Miscs;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public enum EventListener implements Listener {
    INSTANCE;
    @EventHandler
    private void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        PlayerInteractHandler.onPlayerInteract(event);
    }

    @EventHandler
    private void onEntityDamageByEntity(@NotNull EntityDamageByEntityEvent event) {
        HitHandler.onEntityDamageByEntity(event);
    }

    @EventHandler
    private void onPlayerDeath(@NotNull PlayerDeathEvent event) {
        HitHandler.onPlayerDeath(event);
    }

    @EventHandler
    private void onProjectileHit(@NotNull ProjectileHitEvent event) {
        HitHandler.onProjectileHit(event);
    }

    @EventHandler
    private void onItemDamage(@NotNull PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        boolean isGun = Guns.isGun(item);
        boolean isAttachment = Attachments.isAttachment(item);
        boolean isGrapling = false;
        if (Miscs.isMisc(item)) isGrapling = Miscs.getMisc(item) instanceof GraplingHook;
        event.setCancelled(isGun || isAttachment || isGrapling);
    }

    @EventHandler
    private void onHotbarSlotSwich(@NotNull PlayerItemHeldEvent event) {
        InventoryHandler.onHotbarSlotSwitch(event);
    }

    @EventHandler
    private void onPlayerSwapHands(@NotNull PlayerSwapHandItemsEvent event) {
        InventoryHandler.onPlayerSwapHands(event);
    }

    @EventHandler
    private void onPlayerLeave(@NotNull PlayerQuitEvent event) {
        ZoomManager.INSTANCE.zoomOut(event.getPlayer());
    }

    @EventHandler
    private void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        PlayerTrafficHandler.onPlayerJoin(event);
    }

    @EventHandler
    private void onInventoryOpen(@NotNull InventoryOpenEvent event) {
        InventoryHandler.onInventoryOpen(event);
    }

    @EventHandler
    private void onInventoryClick(@NotNull InventoryClickEvent event) {
        InventoryHandler.onInventoryClick(event);
    }

    @EventHandler
    private void onPlayerDropItem(@NotNull PlayerDropItemEvent event) {
        InventoryHandler.onPlayerDropItem(event);
    }

    @EventHandler
    private void onEntityDamage(@NotNull EntityDamageEvent event) {
        HitHandler.onEntityDamage(event);
    }

    @EventHandler
    private void onInventoryClose(@NotNull InventoryCloseEvent event) {
        InventoryHandler.onInventoryClose(event);
    }

    @EventHandler
    private void onPlayerGamemodeChange(@NotNull PlayerGameModeChangeEvent event) {
        ZoomManager.INSTANCE.zoomOut(event.getPlayer());
    }

    @EventHandler
    private void onPlayerItemConsume(@NotNull PlayerItemConsumeEvent event) {
        PlayerItemConsumeHandler.onFinishConsume(event);
    }

    @EventHandler
    private void onFoodLevelChange(@NotNull FoodLevelChangeEvent event) {
        PlayerHealHandler.onFoodLevelChange(event);
    }

    @EventHandler
    private void onEntityRegainHealth(@NotNull EntityRegainHealthEvent event) {
        PlayerHealHandler.onEntityRegainHealth(event);
    }

    @EventHandler
    private void onPlayerStopUsingItem(@NotNull PlayerStopUsingItemEvent event) {
        PlayerItemConsumeHandler.onStopConsume(event);
    }

    @EventHandler
    private void onPlayerFish(@NotNull PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.FISHING) {
            GraplingHandler.onThrow(event);
            return;
        }
        GraplingHandler.onSubtract(event);
    }

    @EventHandler
    private void onPlayerResourcePackStatus(@NotNull PlayerResourcePackStatusEvent event) {
        PlayerTrafficHandler.onPlayerResourcePackStatus(event);
    }
}