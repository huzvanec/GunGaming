package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.loot.SingleLoot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;

import java.util.Random;

public abstract class Gun extends CustomItem implements SingleLoot {

    public Integer shootCooldown = null;

    public Integer reloadCooldown = null;

    public Double damage = null;

    public Float velocity = null;

    public Integer maxAmmo = null;

    public String ammoName = null;
    public Float recoil = null;
    public Float inaccuracy = null;
    private final Random random = new Random();

    public Gun() {
        setup();

        assert shootCooldown != null : "No shoot cooldown given!";
        assert reloadCooldown != null : "No reload cooldown given!";
        assert damage != null : "No damage given!";
        assert velocity != null : "No bullet speed given!";
        assert maxAmmo != null : "No max ammo given!";
        assert ammoName != null : "No ammo name given!";
        assert recoil != null : "No recoil given!";
        assert inaccuracy != null : "No inaccuracy given!";

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

    public Arrow shoot(PlayerInteractEvent event, ItemStack heldItem) {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        Vector recoilVector = location.getDirection().multiply((float) Namespaces.GUN_RECOIL.get(heldItem));
        recoilVector.setY(recoilVector.getY() / 2.5f);
        player.setVelocity(player.getVelocity().subtract(recoilVector));

        Vector arrowVector = player.getLocation().getDirection();
        arrowVector.multiply(velocity);
        randomizeVector(arrowVector, Namespaces.GUN_INACCURACY.get(heldItem));

        Arrow bullet = player.launchProjectile(Arrow.class, arrowVector);
        bullet.setPickupStatus(PickupStatus.DISALLOWED);

        Namespaces.BULLET.set(bullet, ammoName);
        Namespaces.BULLET_DAMAGE.set(bullet, damage);
        Namespaces.BULLET_GUN_NAME.set(bullet, name);

        bullet.setFallDistance(0);
        onShoot(event, bullet);
        return bullet;
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
