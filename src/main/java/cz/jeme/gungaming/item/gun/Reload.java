package cz.jeme.gungaming.item.gun;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.item.tracker.PlayerTracker;
import cz.jeme.gungaming.util.Components;
import cz.jeme.gungaming.util.Inventories;
import net.kyori.adventure.sound.Sound;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.text.DecimalFormat;

@NullMarked
final class Reload extends BukkitRunnable {
    private final ItemStack item;
    private final Material material;
    private final Player player;
    private final int reloadAmmo;
    private final ItemStack ammoItem;
    private final boolean creative;
    private final boolean magazineless;
    private final int reloadCooldown;
    private int reloaded = 0;
    private final Sound sound;
    private final Gun gun;
    private long startTime;

    private final @Nullable ActionRunnable actionRunnable;

    public Reload(final Player player, final ItemStack item, final Gun gun,
                  final int reloadAmmo, final ItemStack ammoItem, final int reloadCooldown) {
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

        final PlayerInventory inventory = player.getInventory();
        final ItemStack mainHand = inventory.getItemInMainHand();
        final boolean mainHandTracker = mainHand != item && CustomItem.is(mainHand, PlayerTracker.class);
        final ItemStack offHand = inventory.getItemInOffHand();
        final boolean offHandTracker = offHand != item && CustomItem.is(offHand, PlayerTracker.class);
        actionRunnable = mainHandTracker || offHandTracker ? null : new ActionRunnable();
        runTaskTimer(GunGaming.instance(), reloadCooldown, reloadCooldown);
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
        if (actionRunnable != null) actionRunnable.cancel();
        super.cancel();
        player.setCooldown(material, 0);
        player.getWorld().stopSound(sound);
        ReloadManager.INSTANCE.removeReload(player);
    }

    public ItemStack item() {
        return item;
    }

    public Gun gun() {
        return gun;
    }

    private class ActionRunnable extends BukkitRunnable {
        private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("0.0");

        private ActionRunnable() {
            runTaskTimer(GunGaming.instance(), 0L, 2L);
        }

        @Override
        public void run() {
            final long currentTime = System.currentTimeMillis();
            final double seconds = (startTime + reloadCooldown * 50L - currentTime) / 1000D;

            player.sendActionBar(Components.of(
                    "<#00FFFF>Reloading [" + DECIMAL_FORMATTER.format(Math.max(seconds, 0)) + "]"
            ));
        }
    }
}
