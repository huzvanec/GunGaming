package cz.jeme.programu.gungaming.runnable;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.TwelveGauge;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.manager.CooldownManager;
import cz.jeme.programu.gungaming.manager.ReloadManager;
import cz.jeme.programu.gungaming.util.Inventories;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.Sounds;
import cz.jeme.programu.gungaming.util.item.Ammos;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Reload extends BukkitRunnable {

    private final Material material;
    public final ItemStack item;
    private final Player player;
    private final int ammoCount;
    private final ReloadManager reloadManager;
    private final CooldownManager cooldownManager;
    private final Ammo ammo;
    private final boolean isCreative;
    private final boolean isShotgun;
    private final int reloadCooldown;
    private int ammoCounter = -1;
    private final Gun gun;

    private final BukkitRunnable actionRunnable = new BukkitRunnable() {
        private int dotsCount = 1;

        @Override
        public void run() {
            String dots = ".".repeat(dotsCount);
            player.sendActionBar(Messages.from(
                    "<dark_aqua>Reloading" + dots + "</dark_aqua>"
            ));

            if (dotsCount < 3) {
                dotsCount++;
            } else {
                dotsCount = 1;
            }
        }
    };

    public Reload(ItemStack gunItem, Player player, ReloadManager reloadManager, int ammoCount, Ammo ammo, Gun gun,
                  boolean isCreative, CooldownManager cooldownManager, int reloadCooldown) {
        this.item = gunItem;
        this.material = gunItem.getType();
        this.reloadManager = reloadManager;
        this.player = player;
        this.ammoCount = ammoCount;
        this.ammo = ammo;
        this.isCreative = isCreative;
        this.cooldownManager = cooldownManager;
        this.reloadCooldown = reloadCooldown;
        this.gun = gun;
        isShotgun = ammo.getClass().equals(TwelveGauge.class);
    }

    @Override
    public void run() {
        ammoCounter++;

        if (ammoCounter == 0) { // First run, don't add any ammo
            newReload();
            actionRunnable.runTaskTimer(GunGaming.getPlugin(), 0, 4);
            return;
        }

        if (ammoCount > ammoCounter && isShotgun) newReload();

        if (isShotgun) {
            shotgun();
        } else {
            normal();
        }
    }

    private void shotgun() {
        if (!isCreative) {
            Inventories.removeItems(player.getInventory(), ammo.item, 1);
        }
        Ammos.add(item, 1);
        if (ammoCounter == ammoCount) {
            cancel();
        }
    }

    private void normal() {
        if (!isCreative) {
            Inventories.removeItems(player.getInventory(), ammo.item, ammoCount);
        }
        Ammos.add(item, ammoCount);
        cancel();
    }

    private void newReload() {
        cooldownManager.setCooldown(player, item.getType(), reloadCooldown);
        player.getWorld().playSound(Sounds.getGunReloadSound(item));
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        actionRunnable.cancel();
        reloadManager.removeReload(player, material);
        super.cancel();
    }
}
