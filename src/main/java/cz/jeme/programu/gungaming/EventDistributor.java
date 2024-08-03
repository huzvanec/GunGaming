package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.game.GameEventHandler;
import cz.jeme.programu.gungaming.item.ItemEventHandler;
import cz.jeme.programu.gungaming.item.attachment.AttachmentEventHandler;
import cz.jeme.programu.gungaming.item.consumable.ConsumableEventHandler;
import cz.jeme.programu.gungaming.item.gun.GunEventHandler;
import cz.jeme.programu.gungaming.item.impl.GrapplingHook;
import cz.jeme.programu.gungaming.item.throwable.ThrowableEventHandler;
import cz.jeme.programu.gungaming.loot.crate.CrateEventHandler;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

public enum EventDistributor implements Listener {
    INSTANCE;

    @EventHandler
    private static void onPlayerInteract(final @NotNull PlayerInteractEvent event) {
        ItemEventHandler.onPlayerInteract(event);
        CrateEventHandler.onPlayerInteract(event);
    }

    @EventHandler
    private static void onPlayerJoin(final @NotNull PlayerJoinEvent event) {
        GameEventHandler.onPlayerJoin(event);
        PlayerTrafficHandler.onPlayerJoin(event); // important: has to go last
    }

    @EventHandler
    private static void onPlayerResourcePackStatus(final @NotNull PlayerResourcePackStatusEvent event) {
        PlayerTrafficHandler.onPlayerResourcePackStatus(event);
    }

    @EventHandler
    private static void onPlayerMove(final @NotNull PlayerMoveEvent event) {
        PlayerTrafficHandler.onPlayerMove(event);
        GameEventHandler.onPlayerMove(event);
    }

    @EventHandler
    private static void onPlayerSwapHandItems(final @NotNull PlayerSwapHandItemsEvent event) {
        GunEventHandler.onPlayerSwapHandItems(event);
    }

    @EventHandler
    private static void onPlayerDropItem(final @NotNull PlayerDropItemEvent event) {
        GunEventHandler.onPlayerDropItem(event);
        AttachmentEventHandler.onPlayerDropItem(event);
    }

    @EventHandler
    private static void onPlayerItemHeld(final @NotNull PlayerItemHeldEvent event) {
        GunEventHandler.onPlayerItemHeld(event);
        ItemEventHandler.onPlayerItemHeld(event);
        AttachmentEventHandler.onPlayerItemHeld(event);
    }

    @EventHandler
    private static void onInventoryOpenEvent(final @NotNull InventoryOpenEvent event) {
        GunEventHandler.onInventoryOpenEvent(event);
    }

    @EventHandler
    private static void onInventoryClick(final @NotNull InventoryClickEvent event) {
        GunEventHandler.onInventoryClick(event);
        AttachmentEventHandler.onInventoryClick(event);
    }

    @EventHandler
    private static void onProjectileHit(final @NotNull ProjectileHitEvent event) {
        GunEventHandler.onProjectileHit(event);
        ThrowableEventHandler.onProjectileHit(event);
    }

    @EventHandler
    private static void onEntityDamageByEntity(final @NotNull EntityDamageByEntityEvent event) {
        GunEventHandler.onEntityDamageByEntity(event);
        ThrowableEventHandler.onEntityDamageByEntity(event);
    }

    @EventHandler
    private static void onEntityDamage(final @NotNull EntityDamageEvent event) {
        GlobalEventHandler.onEntityDamage(event);
        GrapplingHook.onEntityDamage(event);
        GameEventHandler.onEntityDamage(event);
    }

    @EventHandler
    private static void onPrepareItemCraft(final @NotNull PrepareItemCraftEvent event) {
        ItemEventHandler.onPrepareItemCraft(event);
        GameEventHandler.onPrepareItemCraft(event);
    }

    @EventHandler
    private static void onPlayerItemConsume(final @NotNull PlayerItemConsumeEvent event) {
        ConsumableEventHandler.onPlayerItemConsume(event);
    }

    @EventHandler
    private static void onPlayerStopUsingItem(final @NotNull PlayerStopUsingItemEvent event) {
        ConsumableEventHandler.onPlayerStopUsingItem(event);
    }

    @EventHandler
    private static void onInventoryClose(final @NotNull InventoryCloseEvent event) {
        AttachmentEventHandler.onInventoryClose(event);
        GunEventHandler.onInventoryClose(event);
    }

    @EventHandler
    private static void onPlayerQuit(final @NotNull PlayerQuitEvent event) {
        AttachmentEventHandler.onPlayerQuit(event);
        GameEventHandler.onPlayerQuit(event);
    }

    @EventHandler
    private static void onPlayerGameModeChange(final @NotNull PlayerGameModeChangeEvent event) {
        AttachmentEventHandler.onPlayerGameModeChange(event);
    }

    @EventHandler
    private static void onPlayerDeath(final @NotNull PlayerDeathEvent event) {
        AttachmentEventHandler.onPlayerDeath(event);
        GunEventHandler.onPlayerDeath(event);
        GameEventHandler.onPlayerDeath(event);
    }

    @EventHandler
    private static void onPlayerItemDamage(final @NotNull PlayerItemDamageEvent event) {
        GunEventHandler.onPlayerItemDamage(event);
    }

    @EventHandler
    private static void onEntityShootBow(final @NotNull EntityShootBowEvent event) {
        GunEventHandler.onEntityShootBow(event);
    }

    @EventHandler
    private static void onPlayerFish(final @NotNull PlayerFishEvent event) {
        GrapplingHook.onPlayerFish(event);
    }

    @EventHandler
    private static void onEntityToggleGlide(final @NotNull EntityToggleGlideEvent event) {
        GameEventHandler.onEntityToggleGlide(event);
    }

    @EventHandler
    private static void onFoodLevelChange(final @NotNull FoodLevelChangeEvent event) {
        GameEventHandler.onFoodLevelChange(event);
    }

    @EventHandler
    private static void onEntityRegainHealth(final @NotNull EntityRegainHealthEvent event) {
        GameEventHandler.onEntityRegainHealth(event);
    }
}