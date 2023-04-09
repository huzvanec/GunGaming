package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.eventhandlers.*;
import cz.jeme.programu.gungaming.managers.CooldownManager;
import cz.jeme.programu.gungaming.managers.ReloadManager;
import cz.jeme.programu.gungaming.managers.ZoomManager;
import cz.jeme.programu.gungaming.utils.GunUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

public class EventListener implements Listener {

    private final ZoomManager zoomManager;

    private final HitHandler hitHandler;
    private final PlayerInteractHandler interactHandler;
    private final InventoryHandler inventoryHandler;
    private final HotbarSlotSwitchHandler hotbarSlotSwichHandler;
    private final PlayerJoinHandler playerJoinHandler = new PlayerJoinHandler();

    public EventListener(CooldownManager cooldownManager, ZoomManager zoomManager, ReloadManager reloadManager,
                         ArrowVelocityListener arrowVelocityListener) {

        this.zoomManager = zoomManager;

        hitHandler = new HitHandler(arrowVelocityListener);
        interactHandler = new PlayerInteractHandler(cooldownManager, arrowVelocityListener, zoomManager);
        inventoryHandler = new InventoryHandler(reloadManager);
        hotbarSlotSwichHandler = new HotbarSlotSwitchHandler(zoomManager, reloadManager);
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
    private void onProjectileHit(ProjectileHitEvent event) {
        hitHandler.onProjectileHit(event);
    }

    @EventHandler
    private void onItemDamage(PlayerItemDamageEvent event) {
        if (GunUtils.isGun(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onHotbarSlotSwichHandler(PlayerItemHeldEvent event) {
        hotbarSlotSwichHandler.onHotbarSlotSwitch(event);
    }

    @EventHandler
    private void onSwapHands(PlayerSwapHandItemsEvent event) {
        inventoryHandler.onSwapHands(event);
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
}
