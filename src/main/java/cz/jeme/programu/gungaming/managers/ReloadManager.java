package cz.jeme.programu.gungaming.managers;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.items.ammo.Ammo;
import cz.jeme.programu.gungaming.items.guns.Gun;
import cz.jeme.programu.gungaming.runnables.Reload;
import cz.jeme.programu.gungaming.utils.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReloadManager {
    private final CooldownManager cooldownManager;

    private final Map<UUID, Map<Material, Reload>> reloads = new HashMap<>();

    public ReloadManager(CooldownManager cooldownManager) {
        this.cooldownManager = cooldownManager;
    }

    public void reload(Player player, ItemStack item) {
        UUID uuid = player.getUniqueId();
        if (!GunUtils.isGun(item)) {
            return;
        }

        if (AmmoLoreUtils.isAtMaxAmmo(item)) {
            return;
        }
        Material material = item.getType();

        if (!reloads.containsKey(uuid)) {
            reloads.put(uuid, new HashMap<>());
        }

        if (reloads.get(uuid).containsKey(material)) {
            return;
        }

        Gun gun = GunUtils.getGun(item);
        Ammo ammo = AmmoUtils.getAmmo(gun);

        PlayerInventory inventory = player.getInventory();

        int ammoFound = InventoryUtils.getItemCount(inventory, ammo.item);
        int ammoRequired = AmmoLoreUtils.getMaxAmmo(item) - AmmoLoreUtils.getAmmo(item);

        boolean isCreative = player.getGameMode() == GameMode.CREATIVE;

        if (ammoFound == 0 && !isCreative) {
            MessageUtils.actionMessage(player, ChatColor.RED + "Out of ammo!");
            return;
        }

        int ammoAdd;
        if (ammoFound >= ammoRequired || isCreative) {
            ammoAdd = ammoRequired;
        } else {
            ammoAdd = ammoFound;
        }

        cooldownManager.setCooldown(player, gun.item.getType(), gun.reloadCooldown);
        Reload reload = new Reload(item, player, this, ammoAdd, ammo.item, isCreative);
        reloads.get(uuid).put(material, reload);
        reload.runTaskLater(GunGaming.getPlugin(GunGaming.class), gun.reloadCooldown / 50);
        MessageUtils.actionMessage(player, ChatColor.DARK_AQUA + "Reloading...");
    }

    public void abortReload(Player player, ItemStack item) {
        Gun gun = GunUtils.getGun(item);
        UUID uuid = player.getUniqueId();
        if (!reloads.containsKey(uuid)) {
            return;
        }

        Map<Material, Reload> reloadMap = reloads.get(uuid);

        Material material = item.getType();

        if (!reloadMap.containsKey(material)) {
            return;
        }

        cooldownManager.setCooldown(player, gun.item.getType(), 0);
        Reload reload = reloadMap.get(material);
        if (reload != null) {
            reload.cancel();
        }
        removeReload(player, material);
        MessageUtils.actionMessage(player, ChatColor.RED + "Reload aborted");
    }

    public void abortReloads(Player player, boolean actionNotify) {
        UUID uuid = player.getUniqueId();
        if (!reloads.containsKey(uuid)) {
            return;
        }
        Map<Material, Reload> reloadMap = reloads.get(uuid);
        if (reloadMap.size() == 0) {
            return;
        }
        for (Material material : reloadMap.keySet()) {
            cooldownManager.setCooldown(player, material, 0);
            Reload reload = reloadMap.get(material);
            if (reload != null) {
                reload.cancel();
            }
            removeReload(player, material);
            if (actionNotify) {
                MessageUtils.actionMessage(player, ChatColor.RED + "Reload aborted");
            }
        }
    }

    public void abortReloads(Player player) {
        abortReloads(player, true);
    }

    public void removeReload(Player player, Material material) {
        UUID uuid = player.getUniqueId();
        Map<Material, Reload> reloadMap = reloads.get(uuid);
        if (reloadMap.size() == 0) {
            return;
        }
        reloadMap.remove(material);
        MessageUtils.actionMessage(player, "");
    }
}
