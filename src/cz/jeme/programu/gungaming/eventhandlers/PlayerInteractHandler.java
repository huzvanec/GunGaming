package cz.jeme.programu.gungaming.eventhandlers;

import java.util.Arrays;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import cz.jeme.programu.gungaming.ArrowVelocityListener;
import cz.jeme.programu.gungaming.items.guns.Gun;
import cz.jeme.programu.gungaming.managers.CooldownManager;
import cz.jeme.programu.gungaming.managers.ZoomManager;
import cz.jeme.programu.gungaming.utils.AmmoLoreUtils;
import cz.jeme.programu.gungaming.utils.GunUtils;
import cz.jeme.programu.gungaming.utils.LoreUtils;
import cz.jeme.programu.gungaming.utils.Materials;
import cz.jeme.programu.gungaming.utils.MessageUtils;
import net.md_5.bungee.api.ChatColor;

public class PlayerInteractHandler {

	private CooldownManager cooldownManager;

	private ArrowVelocityListener arrowVelocityListener;

	private ZoomManager zoomManager;

	public PlayerInteractHandler(CooldownManager cooldownManager, ArrowVelocityListener arrowVelocityListener,
			ZoomManager zoomManager) {
		this.cooldownManager = cooldownManager;
		this.arrowVelocityListener = arrowVelocityListener;
		this.zoomManager = zoomManager;
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack heldItem = player.getInventory().getItemInMainHand();

		if (!GunUtils.isGun(heldItem)) {
			return;
		}
		Action action = event.getAction();

		if (action == Action.RIGHT_CLICK_AIR) {
			onRightClick(event);
		} else if (action == Action.RIGHT_CLICK_BLOCK) {
			Material material = event.getClickedBlock().getType();
			if (!Arrays.asList(Materials.CONTAINERS).contains(material)) {
				event.setCancelled(true);
				onRightClick(event);
			}
		} else if (action == Action.LEFT_CLICK_AIR) {
			onLeftClick(event);
		} else if (action == Action.LEFT_CLICK_BLOCK) {
			onLeftClick(event);
			event.setCancelled(true);
		}
	}

	private void onRightClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack heldItem = player.getInventory().getItemInMainHand();
		event.setCancelled(true);

		Gun gun = GunUtils.getGun(heldItem);
		if (player.getCooldown(heldItem.getType()) != 0) {
			return;
		}
		int heldAmmo = AmmoLoreUtils.getAmmo(heldItem);
		boolean isCreative = player.getGameMode() == GameMode.CREATIVE;
		if (heldAmmo == 0 && !isCreative) {
			MessageUtils.actionMessage(player, ChatColor.RED + "Reload required");
			return;
		}
		if (!isCreative) {
			AmmoLoreUtils.removeAmmo(heldItem, 1);
		}
		Arrow arrow = gun.shoot(event);
		if (gun.isRocket) {
			arrowVelocityListener.addArrow(arrow);
		}
		cooldownManager.setCooldown(player, gun.item.getType(), gun.shootCooldown);
	}

	private void onLeftClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (!GunUtils.isGun(item)) {
			return;
		}
		event.setCancelled(true);
		String zoomMultiplier = LoreUtils.getLore(item).get("Scope");
		if (zoomMultiplier != null) {
			zoomManager.nextZoom(player, Double.valueOf(zoomMultiplier));
		}
	}
}
