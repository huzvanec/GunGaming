package cz.jeme.programu.gungaming.manager;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.runnable.Reload;
import cz.jeme.programu.gungaming.util.Inventories;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.util.item.Ammos;
import cz.jeme.programu.gungaming.util.item.Guns;
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
        if (!Guns.isGun(item)) {
            return;
        }

        if ((int) Namespaces.GUN_AMMO_CURRENT.get(item) == (int) Namespaces.GUN_AMMO_MAX.get(item)) {
            return;
        }
        Material material = item.getType();

        if (!reloads.containsKey(uuid)) {
            reloads.put(uuid, new HashMap<>());
        }

        if (reloads.get(uuid).containsKey(material)) {
            return;
        }

        Gun gun = Guns.getGun(item);
        Ammo ammo = Ammos.getAmmo(gun);

        PlayerInventory inventory = player.getInventory();

        int ammoFound = Inventories.getItemCount(inventory, ammo.item);
        int ammoRequired = (int) Namespaces.GUN_AMMO_MAX.get(item) - (int) Namespaces.GUN_AMMO_CURRENT.get(item);

        boolean isCreative = player.getGameMode() == GameMode.CREATIVE;

        if (ammoFound == 0 && !isCreative) {
            player.sendActionBar(Messages.from("<red>Out of ammo!</red>"));
            return;
        }

        int ammoAdd;
        if (ammoFound >= ammoRequired || isCreative) {
            ammoAdd = ammoRequired;
        } else {
            ammoAdd = ammoFound;
        }

        int reloadCooldown = Namespaces.GUN_RELOAD_COOLDOWN.get(item);

        cooldownManager.setCooldown(player, item.getType(), reloadCooldown);
        Reload reload = new Reload(item, player, this, ammoAdd, ammo.item, isCreative);
        reloads.get(uuid).put(material, reload);
        reload.runTaskLater(GunGaming.getPlugin(GunGaming.class), reloadCooldown / 50);
        player.sendActionBar(Messages.from("<dark_aqua>Reloading...</dark_aqua>"));
    }

    public void abortReload(Player player, ItemStack item) {
        Gun gun = Guns.getGun(item);
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
        player.sendActionBar(Messages.from("<red>Reload aborted!</red>"));
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
                player.sendActionBar(Messages.from("<red>Reload aborted!</red>"));
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
        player.sendActionBar(Messages.from(""));
    }
}
