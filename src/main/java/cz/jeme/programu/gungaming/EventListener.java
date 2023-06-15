package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.eventhandler.*;
import cz.jeme.programu.gungaming.eventhandler.interaction.PlayerInteractHandler;
import cz.jeme.programu.gungaming.manager.CooldownManager;
import cz.jeme.programu.gungaming.manager.ReloadManager;
import cz.jeme.programu.gungaming.manager.ZoomManager;
import cz.jeme.programu.gungaming.util.item.Attachments;
import cz.jeme.programu.gungaming.util.item.Guns;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

import java.io.File;

public class EventListener implements Listener {

    private final ZoomManager zoomManager;

    private final HitHandler hitHandler;
    private final PlayerInteractHandler interactHandler;
    private final InventoryHandler inventoryHandler;
    private final HotbarSlotSwitchHandler hotbarSlotSwichHandler;
    private final PlayerJoinHandler playerJoinHandler;
    private final DeathHandler deathHandler;

    public EventListener(CooldownManager cooldownManager, ZoomManager zoomManager, ReloadManager reloadManager, File dataFolder) {

        this.zoomManager = zoomManager;

        hitHandler = new HitHandler();
        interactHandler = new PlayerInteractHandler(cooldownManager, zoomManager);
        inventoryHandler = new InventoryHandler(reloadManager, zoomManager);
        hotbarSlotSwichHandler = new HotbarSlotSwitchHandler(zoomManager, reloadManager);
        playerJoinHandler = new PlayerJoinHandler(dataFolder);
        deathHandler = new DeathHandler(reloadManager, zoomManager);
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        interactHandler.onPlayerInteract(event);
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        hitHandler.onEntityDamageByEntity(event);
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        deathHandler.onPlayerDeath(event);
    }

    @EventHandler
    private void onProjectileHit(ProjectileHitEvent event) {
        hitHandler.onProjectileHit(event);
    }

    @EventHandler
    private void onItemDamage(PlayerItemDamageEvent event) {
        if (Guns.isGun(event.getItem()) || Attachments.isAttachment(event.getItem())) event.setCancelled(true);
    }

    @EventHandler
    private void onHotbarSlotSwichHandler(PlayerItemHeldEvent event) {
        hotbarSlotSwichHandler.onHotbarSlotSwitch(event);
    }

    @EventHandler
    private void onPlayerSwapHands(PlayerSwapHandItemsEvent event) {
        inventoryHandler.onPlayerSwapHands(event);
    }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {
        zoomManager.zoomOut(event.getPlayer());
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        playerJoinHandler.onPlayerJoin(event);
    }

    @EventHandler
    private void onInventoryOpen(InventoryOpenEvent event) {
        inventoryHandler.onInventoryOpen(event);
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        inventoryHandler.onInventoryClick(event);
    }

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event) {
        inventoryHandler.onPlayerDropItem(event);
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        hitHandler.onEntityDamage(event);
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent event) {
        inventoryHandler.onInventoryClose(event);
    }

    @EventHandler
    private void onPlayerGamemodeChange(PlayerGameModeChangeEvent event) {
        zoomManager.zoomOut(event.getPlayer());
    }
}
