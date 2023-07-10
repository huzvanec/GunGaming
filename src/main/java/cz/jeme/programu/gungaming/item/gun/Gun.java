package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.loot.SingleLoot;
import cz.jeme.programu.gungaming.util.Packets;
import cz.jeme.programu.gungaming.util.Sounds;
import cz.jeme.programu.gungaming.util.item.Ammos;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Gun extends CustomItem implements SingleLoot {

    public Integer shootCooldown = null;

    public Integer reloadCooldown = null;

    public Double damage = null;

    public Float velocity = null;

    public Integer maxAmmo = null;

    public Class<? extends Ammo> ammoType = null;
    public final Ammo ammo;
    public Float recoil = null;
    public Float inaccuracy = null;
    public Integer bulletsPerShot = 1;
    public Integer bulletCooldown = 1;
    private final Random random = new Random();

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

        Namespaces.GUN.set(item, name);
        Namespaces.GUN_AMMO_MAX.set(item, maxAmmo);
        Namespaces.GUN_AMMO_CURRENT.set(item, 0);
        Namespaces.GUN_RELOAD_COOLDOWN.set(item, reloadCooldown);
        Namespaces.GUN_RECOIL.set(item, recoil);
        Namespaces.GUN_INACCURACY.set(item, inaccuracy);

        Namespaces.GUN_SCOPE.set(item, "");
        Namespaces.GUN_MAGAZINE.set(item, "");
        Namespaces.GUN_STOCK.set(item, "");


        Damageable meta = (Damageable) item.getItemMeta();
        meta.setDamage(getMaterial().getMaxDurability());
        item.setItemMeta(meta);
    }

    @Override
    protected Material getMaterial() {
        return Material.DIAMOND_SHOVEL;
    }

    public void shoot(PlayerInteractEvent event, ItemStack heldItem) {
        shoot(event, heldItem, 1);
    }

    private void shoot(PlayerInteractEvent event, ItemStack heldItem, int round) {
        Player player = event.getPlayer();

        boolean isCreative = player.getGameMode() == GameMode.CREATIVE;
        int heldAmmo = Namespaces.GUN_AMMO_CURRENT.get(heldItem);
        if (heldAmmo == 0 && !isCreative) return;

        if (!isCreative && (bulletCooldown != 0 || round == 1)) {
            Ammos.remove(heldItem, 1);
        }

        Location location = player.getLocation();

        if (bulletCooldown != 0 || round == 1) {
            location.getWorld().playSound(Sounds.getGunShootSound(this));
        }

        Vector recoilVector = location.getDirection().multiply((float) Namespaces.GUN_RECOIL.get(heldItem));
        recoilVector.setY(recoilVector.getY() / 3.5f);
        player.setVelocity(player.getVelocity().subtract(recoilVector));

        Vector arrowVector = player.getLocation().getDirection();
        arrowVector.multiply(velocity);
        randomizeVector(arrowVector, Namespaces.GUN_INACCURACY.get(heldItem));

        Arrow bullet = player.launchProjectile(Arrow.class, arrowVector);
        bullet.setPickupStatus(PickupStatus.DISALLOWED);

        Namespaces.BULLET.set(bullet, ammo.name);
        Namespaces.BULLET_DAMAGE.set(bullet, damage);
        Namespaces.BULLET_GUN_NAME.set(bullet, name);

        bullet.setFallDistance(0);
        onShoot(event, bullet);

        List<Player> players = new ArrayList<>();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                continue;
            }
            players.add(onlinePlayer);
        }
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();
        ClientboundAnimatePacket packet = new ClientboundAnimatePacket(serverPlayer, ClientboundAnimatePacket.SWING_MAIN_HAND);
        Packets.sendPacket(players, packet);

        if (round < bulletsPerShot) {
            Bukkit.getScheduler().runTaskLater(GunGaming.getPlugin(), () -> shoot(event, heldItem, round + 1), bulletCooldown);
        }
    }

    private void randomizeVector(Vector vector, float degrees) {
        float radians = (float) Math.toRadians(degrees);
        vector.rotateAroundX(random.nextFloat(radians * 2) - radians);
        vector.rotateAroundY(random.nextFloat(radians * 2) - radians);
        vector.rotateAroundZ(random.nextFloat(radians * 2) - radians);
    }

    protected void onShoot(PlayerInteractEvent event, Arrow bullet) {
    }

    public void onBulletHit(ProjectileHitEvent event, Projectile bullet) {
    }

    @Override
    public int getMinLoot() {
        return 1;
    }

    @Override
    public int getMaxLoot() {
        return 1;
    }
}
