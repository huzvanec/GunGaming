package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.eventhandler.*;
import cz.jeme.programu.gungaming.eventhandler.interaction.PlayerInteractHandler;
import cz.jeme.programu.gungaming.game.Game;
import cz.jeme.programu.gungaming.item.misc.GraplingHook;
import cz.jeme.programu.gungaming.manager.ZoomManager;
import cz.jeme.programu.gungaming.util.registry.Attachments;
import cz.jeme.programu.gungaming.util.registry.Guns;
import cz.jeme.programu.gungaming.util.registry.Miscs;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * GunGaming global event listener.
 * Used to distribute all the game events.
 */
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
        GraplingHook.onPlayerFish(event);
    }

    @EventHandler
    private void onPlayerResourcePackStatus(@NotNull PlayerResourcePackStatusEvent event) {
        PlayerTrafficHandler.onPlayerResourcePackStatus(event);
    }

    @EventHandler
    private void onPlayerMove(@NotNull PlayerMoveEvent event) {
        Boolean frozen = Namespace.FROZEN.get(event.getPlayer());
        if (frozen != null && frozen && event.hasChangedBlock()) event.setCancelled(true);
        PlayerTrafficHandler.onPlayerMove(event);
    }

    @EventHandler
    private void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        PlayerTrafficHandler.onPlayerJoin(event);
    }

    @EventHandler
    private void onEntityToggleGlide(@NotNull EntityToggleGlideEvent event) {
        Game.onEntityToggleGlide(event);
    }

    @EventHandler
    private void onWeatherChange(@NotNull WeatherChangeEvent event) {
        if (Game.isRunning()) event.setCancelled(true);
    }

    @EventHandler
    private void onEntitySpawn(@NotNull CreatureSpawnEvent event) {
        if (Game.isRunning() && Game.DISABLED_SPAWNING.contains(event.getSpawnReason())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerPortal(@NotNull PlayerPortalEvent event) {
        if (Game.isRunning()) event.setCancelled(true);
    }

    @EventHandler
    private void onEntityPortal(@NotNull EntityPortalEvent event) {
        if (Game.isRunning()) event.setCancelled(true);
    }
}