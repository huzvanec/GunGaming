package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.util.Components;
import cz.jeme.programu.gungaming.util.Inventories;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public enum ReloadManager {
    INSTANCE;
    private final @NotNull Map<UUID, Reload> reloads = new HashMap<>();

    public void reload(final @NotNull Player player, final @NotNull ItemStack item) {
        if (reloads.containsKey(player.getUniqueId())) return;
        if (player.hasCooldown(item.getType())) return;
        final int currentAmmo = Gun.CURRENT_AMMO_DATA.require(item);
        final int maxAmmo = Gun.MAX_AMMO_DATA.require(item);
        if (currentAmmo == maxAmmo) return;
        final int reloadCooldown = Gun.RELOAD_DURATION_DATA.require(item);

        final Gun gun = Gun.of(item);
        final Ammo ammo = gun.ammo();

        final PlayerInventory inventory = player.getInventory();

        final int ammoFound = Inventories.count(inventory, ammo.item());
        final int ammoNeeded = maxAmmo - currentAmmo;

        final boolean creative = player.getGameMode() == GameMode.CREATIVE;

        // the player has no ammo && the player is not in creative
        if (ammoFound == 0 && !creative) {
            player.sendActionBar(Components.of("<red>Out of ammo!"));
            player.getWorld().playSound(gun.outOfAmmoSound(item), player);
            return;
        }

        final int reloadAmmo = creative ? ammoNeeded : Math.min(ammoFound, ammoNeeded);

        final Reload reload = new Reload(player, item, gun, reloadAmmo, ammo.item(), reloadCooldown);
        reloads.put(player.getUniqueId(), reload);
    }

    public void abortReload(final @NotNull Player player, final boolean actionNotify) {
        final UUID uuid = player.getUniqueId();
        final Reload reload = reloads.get(uuid);
        if (reload == null) return;
        reload.cancel();
        if (actionNotify) {
            player.getWorld().playSound(reload.gun().reloadAbortedSound(reload.item()), player);
            player.sendActionBar(Components.of("<red>Reload aborted!"));
        }
    }

    public void abortReloadAll(final boolean actionNotify) {
        Bukkit.getOnlinePlayers().forEach(player -> abortReload(player, actionNotify));
    }

    public void removeReload(final @NotNull Player player) {
        final UUID uuid = player.getUniqueId();
        reloads.remove(uuid);
        player.sendActionBar(Component.text(""));
    }

    public boolean isReloading(final @NotNull Player player) {
        return reloads.containsKey(player.getUniqueId());
    }
}
