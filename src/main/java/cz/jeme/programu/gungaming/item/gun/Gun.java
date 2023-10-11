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

    public @NotNull Integer shootCooldown;

    public @NotNull Integer reloadCooldown;

    public @NotNull Double damage;

    public @NotNull Float velocity;

    public @NotNull Integer maxAmmo;

    public @NotNull Class<? extends Ammo> ammoType;
    public final @NotNull Ammo ammo;
    public @NotNull Float recoil;
    public @NotNull Float inaccuracy;
    public @NotNull Integer bulletsPerShot = 1;
    public @NotNull Integer bulletCooldown = 1;
    private static final @NotNull Random RANDOM = new Random();

    public Gun() {
        setup();

        assert shootCooldown != null : "No shoot cooldown given!";
        assert reloadCooldown != null : "No reload cooldown given!";
        assert damage != null : "No damage given!";
        assert velocity != null : "No bullet speed given!";
        assert maxAmmo != null : "No max ammo given!";
        assert ammoType != null : "No ammo type given!";
        assert recoil != null : "No recoil given!";
        assert inaccuracy != null : "No inaccuracy given!";

        ammo = Ammos.getAmmo(ammoType);

        Namespace.GUN.set(item, name);
        Namespace.GUN_AMMO_MAX.set(item, maxAmmo);
        Namespace.GUN_AMMO_CURRENT.set(item, 0);
        Namespace.GUN_RELOAD_COOLDOWN.set(item, reloadCooldown);
        Namespace.GUN_RECOIL.set(item, recoil);
        Namespace.GUN_INACCURACY.set(item, inaccuracy);

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

    @Override
    protected @NotNull Material getMaterial() {
        return Material.CROSSBOW;
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

        if (!isCreative && (bulletCooldown > 0 || round == 1)) {
            Ammos.remove(item, 1);
        }

        Location location = player.getLocation();

        if (bulletCooldown != 0 || round == 1) {
            location.getWorld().playSound(Sounds.getGunShootSound(this), player);
        }

        Float gunRecoil = Namespace.GUN_RECOIL.get(item);
        assert gunRecoil != null : "Gun recoil is null!";

        Vector recoilVector = location.getDirection().multiply(gunRecoil);
        recoilVector.setY(recoilVector.getY() / 3.5f);
        player.setVelocity(player.getVelocity().subtract(recoilVector));

        Vector arrowVector = player.getLocation().getDirection();
        arrowVector.multiply(velocity);

        Float gunInaccuracy = Namespace.GUN_INACCURACY.get(item);
        assert gunInaccuracy != null : "Gun inaccuracy is null!";

        randomizeVector(arrowVector, gunInaccuracy);

        Arrow bullet = player.launchProjectile(Arrow.class, arrowVector);
        bullet.setPickupStatus(PickupStatus.DISALLOWED);

        Namespace.BULLET.set(bullet, ammo.name);
        Namespace.BULLET_DAMAGE.set(bullet, damage);
        Namespace.BULLET_GUN_NAME.set(bullet, name);

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

        if (round < bulletsPerShot) {
            Bukkit.getScheduler().runTaskLater(GunGaming.getPlugin(), () -> shoot(event, item, round + 1), bulletCooldown);
        }
    }

    private void randomizeVector(@NotNull Vector vector, float degrees) {
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

    @Override
    public final int getMinLoot() {
        return 1;
    }

    @Override
    public final int getMaxLoot() {
        return 1;
    }
}
