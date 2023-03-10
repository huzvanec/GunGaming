package cz.jeme.programu.gungaming;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import cz.jeme.programu.gungaming.eventhandlers.HitHandler;
import cz.jeme.programu.gungaming.eventhandlers.HotbarSlotSwichHandler;
import cz.jeme.programu.gungaming.eventhandlers.InventoryHandler;
import cz.jeme.programu.gungaming.eventhandlers.PlayerInteractHandler;
import cz.jeme.programu.gungaming.eventhandlers.PlayerJoinHandler;
import cz.jeme.programu.gungaming.managers.CooldownManager;
import cz.jeme.programu.gungaming.managers.ReloadManager;
import cz.jeme.programu.gungaming.managers.ZoomManager;
import cz.jeme.programu.gungaming.utils.GunUtils;

public class EventListener implements Listener {

	private ZoomManager zoomManager;

	private HitHandler hitHandler;
	private PlayerInteractHandler interactHandler;
	private InventoryHandler inventoryHandler;
	private HotbarSlotSwichHandler hotbarSlotSwichHandler;
	private PlayerJoinHandler playerJoinHandler = new PlayerJoinHandler();

	public EventListener(CooldownManager cooldownManager, ZoomManager zoomManager, ReloadManager reloadManager,
			ArrowVelocityListener arrowVelocityListener) {

		this.zoomManager = zoomManager;

		hitHandler = new HitHandler(arrowVelocityListener);
		interactHandler = new PlayerInteractHandler(cooldownManager, arrowVelocityListener, zoomManager);
		inventoryHandler = new InventoryHandler(reloadManager);
		hotbarSlotSwichHandler = new HotbarSlotSwichHandler(zoomManager, reloadManager);
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
		hotbarSlotSwichHandler.onHotbarSlotSwich(event);
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
