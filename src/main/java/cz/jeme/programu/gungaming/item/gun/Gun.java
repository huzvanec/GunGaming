package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.util.Namespaces;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.Damageable;

public abstract class Gun extends CustomItem {

    public Integer shootCooldown = null;

    public Integer reloadCooldown = null;

    public Double damage = null;

    public Float velocity = null;

    public Integer maxAmmo = null;

    public String ammoName = null;

    public Gun() {
        setup();

        assert shootCooldown != null : "No shoot cooldown given!";
        assert reloadCooldown != null : "No reload cooldown given!";
        assert damage != null : "No damage given!";
        assert velocity != null : "No bullet speed given!";
        assert maxAmmo != null : "No max ammo given!";
        assert ammoName != null : "No ammo name given!";

        Namespaces.GUN.set(item, name);
        Namespaces.MAX_GUN_AMMO.set(item, maxAmmo);
        Namespaces.CURRENT_GUN_AMMO.set(item, 0);

        Namespaces.GUN_SCOPE.set(item, "");
        Namespaces.GUN_MAGAZINE.set(item, "");
        Namespaces.GUN_STOCK.set(item, "");


        Damageable meta = (Damageable) item.getItemMeta();
        meta.setDamage(material.getMaxDurability());
        item.setItemMeta(meta);
    }

    public Arrow shoot(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Arrow bullet = player.launchProjectile(Arrow.class, player.getLocation().getDirection().multiply(velocity));
        bullet.setPickupStatus(PickupStatus.DISALLOWED);

        Namespaces.BULLET.set(bullet, ammoName);
        Namespaces.BULLET_DAMAGE.set(bullet, damage);
        Namespaces.BULLET_GUN_NAME.set(bullet, name);

        bullet.setFallDistance(0);
        onShoot(event, bullet);
        return bullet;
    }

    protected void onShoot(PlayerInteractEvent event, Arrow bullet) {
    }

    public void onBulletHit(ProjectileHitEvent event, Projectile bullet) {
    }
}
