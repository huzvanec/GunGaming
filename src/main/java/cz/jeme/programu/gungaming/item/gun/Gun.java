package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.loot.SingletonLoot;
import cz.jeme.programu.gungaming.util.Packets;
import cz.jeme.programu.gungaming.util.Sounds;
import cz.jeme.programu.gungaming.util.registry.Ammos;
import cz.jeme.programu.gungaming.util.registry.Guns;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public abstract class Gun extends CustomItem implements SingletonLoot {
    public abstract int getShootCooldown();

    public abstract int getReloadCooldown();

    public abstract double getDamage();

    public abstract float getVelocity();

    public abstract int getMaxAmmo();

    public abstract @NotNull Class<? extends Ammo> getAmmoType();

    private final @NotNull Ammo ammo;

    public final @NotNull Ammo getAmmo() {
        return ammo;
    }

    public abstract float getRecoil();

    public abstract float getInaccuracy();

    public int getBulletsPerShot() {
        return 1;
    }

    public int getBulletCooldown() {
        return 1;
    }

    @Override
    public final @NotNull Material getMaterial() {
        return Material.CROSSBOW;
    }

    @Override
    public final int getMinStackLoot() {
        return 1;
    }

    @Override
    public final int getMaxStackLoot() {
        return 1;
    }

    private static final @NotNull Random RANDOM = new Random();

    public Gun() {
        ammo = Ammos.getAmmo(getAmmoType());

        Namespace.GUN.set(item, getName());
        Namespace.GUN_AMMO_MAX.set(item, getMaxAmmo());
        Namespace.GUN_AMMO_CURRENT.set(item, 0);
        Namespace.GUN_RELOAD_COOLDOWN.set(item, getReloadCooldown());
        Namespace.GUN_RECOIL.set(item, getRecoil());
        Namespace.GUN_INACCURACY.set(item, getInaccuracy());

        Namespace.GUN_SCOPE.set(item, "");
        Namespace.GUN_MAGAZINE.set(item, "");
        Namespace.GUN_STOCK.set(item, "");


        Damageable damageableMeta = (Damageable) item.getItemMeta();
        damageableMeta.setDamage(getMaterial().getMaxDurability());
        item.setItemMeta(damageableMeta);

        CrossbowMeta crossbowMeta = (CrossbowMeta) item.getItemMeta();
        crossbowMeta.addChargedProjectile(new ItemStack(Material.ARROW));
        item.setItemMeta(crossbowMeta);
    }

    public final void shoot(@NotNull PlayerInteractEvent event, @NotNull ItemStack heldItem) {
        shoot(event, heldItem, 1);
    }

    private void shoot(@NotNull PlayerInteractEvent event, @NotNull ItemStack item, int round) {
        Player player = event.getPlayer();

        boolean isCreative = player.getGameMode() == GameMode.CREATIVE;
        Integer heldAmmo = Namespace.GUN_AMMO_CURRENT.get(item);
        assert heldAmmo != null : "Held ammo is null!";
        if (heldAmmo == 0 && !isCreative) return;

        if (!isCreative && (getBulletCooldown() > 0 || round == 1)) {
            Ammos.remove(item, 1);
        }

        Location location = player.getLocation();

        if (getBulletCooldown() != 0 || round == 1) {
            location.getWorld().playSound(Sounds.getGunShootSound(this), player);
        }

        Float gunRecoil = Namespace.GUN_RECOIL.get(item);
        assert gunRecoil != null : "Gun recoil is null!";

        Vector recoilVector = location.getDirection().multiply(gunRecoil);
        recoilVector.setY(recoilVector.getY() / 3.5f);
        player.setVelocity(player.getVelocity().subtract(recoilVector));

        Vector arrowVector = player.getLocation().getDirection();
        arrowVector.multiply(getVelocity());

        Float gunInaccuracy = Namespace.GUN_INACCURACY.get(item);
        assert gunInaccuracy != null : "Gun inaccuracy is null!";

        randomizeVector(arrowVector, gunInaccuracy);

        Arrow bullet = player.launchProjectile(Arrow.class, arrowVector);
        bullet.setPickupStatus(PickupStatus.DISALLOWED);

        Namespace.BULLET.set(bullet, ammo.getName());
        Namespace.BULLET_DAMAGE.set(bullet, getDamage());
        Namespace.BULLET_GUN_NAME.set(bullet, getName());

        bullet.setFallDistance(0);
        onShoot(event, bullet);

        List<? extends Player> players = Bukkit.getOnlinePlayers().stream()
                .filter(onlinePlayer -> !onlinePlayer.getUniqueId().equals(player.getUniqueId()))
                .toList();
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();
        final int hand;
        if (Guns.isGun(player.getInventory().getItemInMainHand())) {
            hand = ClientboundAnimatePacket.SWING_MAIN_HAND;
        } else {
            hand = ClientboundAnimatePacket.SWING_OFF_HAND;
        }
        ClientboundAnimatePacket packet = new ClientboundAnimatePacket(serverPlayer, hand);
        Packets.sendPacket(players, packet);

        if (round < getBulletsPerShot()) {
            Bukkit.getScheduler().runTaskLater(
                    GunGaming.getPlugin(),
                    () -> shoot(event, item, round + 1),
                    getBulletCooldown()
            );
        }
    }

    private static void randomizeVector(@NotNull Vector vector, float degrees) {
        float radians = (float) Math.toRadians(degrees);
        vector.rotateAroundX(RANDOM.nextFloat(radians * 2) - radians);
        vector.rotateAroundY(RANDOM.nextFloat(radians * 2) - radians);
        vector.rotateAroundZ(RANDOM.nextFloat(radians * 2) - radians);
    }

    public final void bulletHit(@NotNull ProjectileHitEvent event, @NotNull Projectile bullet) {
        bullet.remove();
        onBulletHit(event, bullet);
    }

    protected void onShoot(@NotNull PlayerInteractEvent event, @NotNull Arrow bullet) {
    }

    protected void onBulletHit(@NotNull ProjectileHitEvent event, @NotNull Projectile bullet) {
    }
}
