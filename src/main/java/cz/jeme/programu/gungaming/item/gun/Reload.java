package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.util.Components;
import cz.jeme.programu.gungaming.util.Inventories;
import net.kyori.adventure.sound.Sound;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

final class Reload extends BukkitRunnable {
    private final @NotNull ItemStack item;
    private final @NotNull Material material;
    private final @NotNull Player player;
    private final int reloadAmmo;
    private final @NotNull ItemStack ammoItem;
    private final boolean creative;
    private final boolean magazineless;
    private final int reloadCooldown;
    private int reloaded = 0;
    private final @NotNull Sound sound;
    private final @NotNull Gun gun;
    private long startTime;

    private final @NotNull BukkitRunnable actionRunnable = new BukkitRunnable() {
        private static final @NotNull DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("0.0");

        @Override
        public void run() {
            final long currentTime = System.currentTimeMillis();
            final double seconds = (startTime + reloadCooldown * 50L - currentTime) / 1000D;

            player.sendActionBar(Components.of(
                    "<#00FFFF>Reloading [" + DECIMAL_FORMATTER.format(Math.max(seconds, 0)) + "]"
            ));
        }
    };

    public Reload(final @NotNull Player player, final @NotNull ItemStack item, final @NotNull Gun gun,
                  final int reloadAmmo, @NotNull final ItemStack ammoItem, final int reloadCooldown) {
        this.item = item;
        this.gun = gun;
        material = item.getType();
        this.player = player;
        this.reloadAmmo = reloadAmmo;
        magazineless = gun.magazineless();
        creative = player.getGameMode() == GameMode.CREATIVE;
        this.ammoItem = ammoItem;
//        this.reloadCooldown = magazineless ? reloadCooldown : reloadCooldown + 50;
        this.reloadCooldown = reloadCooldown;
        sound = gun.reloadSound(item);
        newReload();
        startTime = System.currentTimeMillis();
        actionRunnable.runTaskTimer(GunGaming.plugin(), 0L, 2L);
        runTaskTimer(GunGaming.plugin(), reloadCooldown, reloadCooldown);
    }

    @Override
    public void run() {
        reloaded++;
        if (reloadAmmo > reloaded && magazineless) newReload();

        if (!Gun.is(item)) {
            ReloadManager.INSTANCE.abortReload(player, true);
            return;
        }
        if (magazineless) shotgun();
        else normal();
    }

    private void shotgun() {
        if (!creative) {
            Inventories.remove(player.getInventory(), ammoItem, 1);
        }
        Gun.addAmmo(item, 1);
        startTime = System.currentTimeMillis();
        if (reloaded == reloadAmmo) {
            cancel();
        }
    }

    private void normal() {
        if (!creative) {
            Inventories.remove(player.getInventory(), ammoItem, reloadAmmo);
        }
        Gun.addAmmo(item, reloadAmmo);
        cancel();
    }

    private void newReload() {
        player.setCooldown(material, reloadCooldown);
        player.getWorld().playSound(sound, player);
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        actionRunnable.cancel();
        super.cancel();
        player.setCooldown(material, 0);
        player.getWorld().stopSound(sound);
        ReloadManager.INSTANCE.removeReload(player);
    }

    public @NotNull ItemStack item() {
        return item;
    }

    public @NotNull Gun gun() {
        return gun;
    }
}
