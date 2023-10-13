package cz.jeme.programu.gungaming.runnable;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.item.gun.Magazineless;
import cz.jeme.programu.gungaming.manager.CooldownManager;
import cz.jeme.programu.gungaming.manager.ReloadManager;
import cz.jeme.programu.gungaming.util.Inventories;
import cz.jeme.programu.gungaming.util.Message;
import cz.jeme.programu.gungaming.util.Sounds;
import cz.jeme.programu.gungaming.util.registry.Ammos;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public final class Reload extends BukkitRunnable {

    private final @NotNull Material material;
    public final @NotNull ItemStack item;
    private final @NotNull Player player;
    private final int ammoCount;
    private final @NotNull ReloadManager reloadManager = ReloadManager.INSTANCE;
    private final @NotNull CooldownManager cooldownManager = CooldownManager.INSTANCE;
    private final @NotNull Ammo ammo;
    private final boolean isCreative;
    private final boolean magazineless;
    private final int reloadCooldown;
    private int ammoCounter = -1;

    private final @NotNull BukkitRunnable actionRunnable = new BukkitRunnable() {
        private int dotsCount = 1;

        @Override
        public void run() {
            String dots = ".".repeat(dotsCount);
            player.sendActionBar(Message.from(
                    "<dark_aqua>Reloading" + dots + "</dark_aqua>"
            ));

            if (dotsCount < 3) {
                dotsCount++;
            } else {
                dotsCount = 1;
            }
        }
    };

    public Reload(@NotNull ItemStack gunItem, @NotNull Player player, int ammoCount, @NotNull Ammo ammo,
                  @NotNull Gun gun, boolean isCreative, int reloadCooldown) {
        this.item = gunItem;
        this.material = gunItem.getType();
        this.player = player;
        this.ammoCount = ammoCount;
        this.ammo = ammo;
        this.isCreative = isCreative;
        magazineless = gun instanceof Magazineless;
        if (magazineless) reloadCooldown += 50; // To prevent shoot glitching when reloading per magazine add one tick
        this.reloadCooldown = reloadCooldown;
    }

    @Override
    public void run() {
        ammoCounter++;

        if (ammoCounter == 0) { // First run, don't add any ammo
            newReload();
            actionRunnable.runTaskTimer(GunGaming.getPlugin(), 0L, 4L);
            return;
        }

        if (ammoCount > ammoCounter && magazineless) newReload();

        if (magazineless) {
            shotgun();
        } else {
            normal();
        }
    }

    private void shotgun() {
        if (!isCreative) {
            Inventories.removeItems(player.getInventory(), ammo.getItem(), 1);
        }
        Ammos.add(item, 1);
        if (ammoCounter == ammoCount) {
            cancel();
        }
    }

    private void normal() {
        if (!isCreative) {
            Inventories.removeItems(player.getInventory(), ammo.getItem(), ammoCount);
        }
        Ammos.add(item, ammoCount);
        cancel();
    }

    private void newReload() {
        cooldownManager.setCooldown(player, item.getType(), reloadCooldown);
        player.getWorld().playSound(Sounds.getGunReloadSound(item), player);
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        actionRunnable.cancel();
        reloadManager.removeReload(player, material);
        super.cancel();
    }
}
