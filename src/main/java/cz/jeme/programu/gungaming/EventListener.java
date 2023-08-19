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

import java.io.File;

public class EventListener implements Listener {

    private final @NotNull ZoomManager zoomManager = ZoomManager.getInstance();
    private final @NotNull HitHandler hitHandler = new HitHandler();
    private final @NotNull PlayerInteractHandler interactHandler;
    private final @NotNull InventoryHandler inventoryHandler = new InventoryHandler();
    private final @NotNull HotbarSlotSwitchHandler hotbarSlotSwichHandler = new HotbarSlotSwitchHandler();
    private final @NotNull PlayerJoinHandler playerJoinHandler;
    private final @NotNull DeathHandler deathHandler = new DeathHandler();
    private final @NotNull PlayerItemConsumeHandler consumeHandler = new PlayerItemConsumeHandler();
    private final @NotNull PlayerHealHandler foodHandler = new PlayerHealHandler();
    private final @NotNull GraplingHandler graplingHandler = new GraplingHandler();

    {
        interactHandler = new PlayerInteractHandler(consumeHandler);
    }

    public EventListener(File dataFolder) {
        playerJoinHandler = new PlayerJoinHandler(dataFolder);
    }

    @EventHandler
    private void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        interactHandler.onPlayerInteract(event);
    }

    @EventHandler
    private void onEntityDamageByEntity(@NotNull EntityDamageByEntityEvent event) {
        hitHandler.onEntityDamageByEntity(event);
    }

    @EventHandler
    private void onPlayerDeath(@NotNull PlayerDeathEvent event) {
        deathHandler.onPlayerDeath(event);
    }

    @EventHandler
    private void onProjectileHit(@NotNull ProjectileHitEvent event) {
        hitHandler.onProjectileHit(event);
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
    private void onHotbarSlotSwichHandler(@NotNull PlayerItemHeldEvent event) {
        hotbarSlotSwichHandler.onHotbarSlotSwitch(event);
    }

    @EventHandler
    private void onPlayerSwapHands(@NotNull PlayerSwapHandItemsEvent event) {
        inventoryHandler.onPlayerSwapHands(event);
    }

    @EventHandler
    private void onPlayerLeave(@NotNull PlayerQuitEvent event) {
        zoomManager.zoomOut(event.getPlayer());
    }

    @EventHandler
    private void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        playerJoinHandler.onPlayerJoin(event);
    }

    @EventHandler
    private void onInventoryOpen(@NotNull InventoryOpenEvent event) {
        inventoryHandler.onInventoryOpen(event);
    }

    @EventHandler
    private void onInventoryClick(@NotNull InventoryClickEvent event) {
        inventoryHandler.onInventoryClick(event);
    }

    @EventHandler
    private void onPlayerDropItem(@NotNull PlayerDropItemEvent event) {
        inventoryHandler.onPlayerDropItem(event);
    }

    @EventHandler
    private void onEntityDamage(@NotNull EntityDamageEvent event) {
        hitHandler.onEntityDamage(event);
    }

    @EventHandler
    private void onInventoryClose(@NotNull InventoryCloseEvent event) {
        inventoryHandler.onInventoryClose(event);
    }

    @EventHandler
    private void onPlayerGamemodeChange(@NotNull PlayerGameModeChangeEvent event) {
        zoomManager.zoomOut(event.getPlayer());
    }

    @EventHandler
    private void onPlayerItemConsume(@NotNull PlayerItemConsumeEvent event) {
        consumeHandler.onFinishConsume(event);
    }

    @EventHandler
    private void onFoodLevelChange(@NotNull FoodLevelChangeEvent event) {
        foodHandler.onFoodLevelChange(event);
    }

    @EventHandler
    private void onEntityRegainHealth(@NotNull EntityRegainHealthEvent event) {
        foodHandler.onEntityRegainHealth(event);
    }

    @EventHandler
    private void onPlayerStopUsingItem(@NotNull PlayerStopUsingItemEvent event) {
        consumeHandler.onStopConsume(event);
    }

    @EventHandler
    private void onPlayerFish(@NotNull PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.FISHING) {
            graplingHandler.onThrow(event);
            return;
        }
        graplingHandler.onSubtract(event);
    }
}
