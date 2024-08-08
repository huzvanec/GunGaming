package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.GlobalEventHandler;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.util.Materials;
import net.kyori.adventure.sound.SoundStop;
import net.minecraft.world.inventory.InventoryMenu;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public final class GunEventHandler {
    private GunEventHandler() {
        throw new AssertionError();
    }

    public static void onPlayerSwapHandItems(final @NotNull PlayerSwapHandItemsEvent event) {
        final Player player = event.getPlayer();
        final PlayerInventory inventory = player.getInventory();
        final ItemStack mainHand = inventory.getItemInMainHand();
        final ItemStack offHand = inventory.getItemInOffHand();
        if (Gun.is(mainHand)) {
            ReloadManager.INSTANCE.reload(player, mainHand);
            event.setCancelled(true);
        } else if (Gun.is(offHand)) {
            ReloadManager.INSTANCE.reload(player, offHand);
            event.setCancelled(true);
        }
    }

    public static void onInventoryOpenEvent(final @NotNull InventoryOpenEvent event) {
        ReloadManager.INSTANCE.abortReload((Player) event.getPlayer(), true);
    }

    public static void onPlayerDropItem(final @NotNull PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        ReloadManager.INSTANCE.abortReload(event.getPlayer(), true);
        final StatsMenu menu = StatsMenuManager.INSTANCE.getMenu(player);
        if (menu == null) return;
        menu.playerDropItem(event);
    }

    public static void onPlayerItemHeld(final @NotNull PlayerItemHeldEvent event) {
        ReloadManager.INSTANCE.abortReload(event.getPlayer(), true);
    }

    public static void onInventoryClick(final @NotNull InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof final Player player)) return;
        ReloadManager.INSTANCE.abortReload(player, true);


        final ItemStack item = event.getCurrentItem();
        final boolean shiftRightClick = event.getClick() == ClickType.SHIFT_RIGHT;
        final boolean emptyHand = event.getCursor().isEmpty();
        final boolean playerInventory = event.getClickedInventory() instanceof PlayerInventory;
        final boolean isGun = Gun.is(item);

        if (shiftRightClick && emptyHand && playerInventory && isGun) {
            event.setCancelled(true);
            StatsMenuManager.INSTANCE.createMenu(player, item);
            return;
        }

        final StatsMenu menu = StatsMenuManager.INSTANCE.getMenu(player);
        if (menu != null && event.getInventory() == menu.inventory()) {
            menu.inventoryClick(event);
            return;
        }
    }

    public static void onInventoryClose(final @NotNull InventoryCloseEvent event) {
        final HumanEntity player = event.getPlayer();
        final StatsMenu menu = StatsMenuManager.INSTANCE.getMenu(player);
        if (menu == null) return;
        if (event.getInventory() != menu.inventory()) return;
        // fix client-server desync after closing the stats menu
        final InventoryMenu inventoryMenu = ((CraftHumanEntity) player).getHandle().inventoryMenu;
        inventoryMenu.resumeRemoteUpdates();
        inventoryMenu.sendAllDataToRemote();
        // remove attachment menu
        StatsMenuManager.INSTANCE.removeMenu(player);
    }

    public static void onPlayerDeath(final @NotNull PlayerDeathEvent event) {
        ReloadManager.INSTANCE.abortReload(event.getEntity(), false);
    }

    public static void onProjectileHit(final @NotNull ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof final AbstractArrow bullet)) return;
        if (!BulletHelper.isBullet(bullet)) return;
        final Gun gun = Gun.of(BulletHelper.GUN_KEY_DATA.require(bullet));

        final Block block = event.getHitBlock();

        if (block == null) {
            // needs to be scheduled after 1 tick, because the location of the bullet has not yet been processed by the server
            Bukkit.getScheduler().runTask(
                    GunGaming.plugin(),
                    () -> gun.onBulletHit(event, bullet)
            );
            bullet.remove();
            return;
        }

        bullet.setGravity(true);
        bullet.setVelocity(new Vector().zero());

        final Material material = block.getType();
        final World world = block.getWorld();
        final Location particleLocation = block.getLocation().add(0.5, 0.5, 0.5);
        if (Materials.isGunBreakable(material)) {
            world.spawnParticle(Particle.BLOCK, particleLocation, 40, .2, .2, .2, .1, block.getBlockData());
            world.playSound(particleLocation, block.getBlockSoundGroup().getBreakSound(), 2, 1);
            block.setType(Material.AIR);
            // the bullet has to be removed after destroying the block to prevent a visual glitch (entity desync)
            bullet.remove();
            return;
        }

        // needs to be scheduled after 1 tick, because the location of the bullet has not yet been processed by the server
        Bukkit.getScheduler().runTask(GunGaming.plugin(), () -> {
            final Location location = bullet.getLocation();
            world.spawnParticle(Particle.BLOCK, location, 5, 0, 0, 0, .05, block.getBlockData());
            gun.onBulletHit(event, bullet);
        });
    }

    public static void onEntityDamageByEntity(final @NotNull EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof final AbstractArrow bullet) || !BulletHelper.isBullet(bullet)) {
            if (event.getDamager() instanceof Projectile)
                GlobalEventHandler.resetNoDamageTicks(event.getEntity());
            return;
        }

        if (event.getEntity() instanceof final LivingEntity livingEntity)
            livingEntity.setMaximumNoDamageTicks(0);

        event.setDamage(BulletHelper.DAMAGE_DATA.require(bullet));
    }

    public static void onPlayerItemDamage(final @NotNull PlayerItemDamageEvent event) {
        if (!Gun.is(event.getItem())) return;
        event.setCancelled(true);
    }

    public static void onEntityShootBow(final @NotNull EntityShootBowEvent event) {
        if (!Gun.is(event.getBow())) return;
        event.setCancelled(true);
        event.getBow().editMeta(CrossbowMeta.class, meta -> meta.setChargedProjectiles(Gun.PROJECTILES));
        event.getEntity().stopSound(SoundStop.named(org.bukkit.Sound.ITEM_CROSSBOW_SHOOT.key()));
    }
}
