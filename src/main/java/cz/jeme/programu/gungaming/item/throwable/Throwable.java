package cz.jeme.programu.gungaming.item.throwable;

import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.util.Sounds;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract class Throwable extends CustomItem {

    public Integer throwCooldown = null;
    public Double damage = null;

    public Throwable() {
        setup();

        assert throwCooldown != null : "Throw cooldown not given!";
        assert damage != null : "Damage not given!";

        Namespaces.THROWABLE.set(item, name);
    }

    @Override
    protected Material getMaterial() {
        return Material.SNOWBALL;
    }

    public final void doThrow(PlayerInteractEvent event, ItemStack heldItem) {
        Player player = event.getPlayer();

        boolean isCreative = player.getGameMode() == GameMode.CREATIVE;
        if (!isCreative) heldItem.setAmount(heldItem.getAmount() - 1);

        Vector thrownVector = player.getLocation().getDirection();

        ThrowableProjectile thrown = player.launchProjectile(Snowball.class, thrownVector);
        Namespaces.THROWN.set(thrown, name);
        Namespaces.THROWN_DAMAGE.set(thrown, damage);

        player.getWorld().playSound(Sounds.getThrowableThrowSound(this), player);

        onDoThrow(event, heldItem, thrown);
    }

    public final void thrownHit(ProjectileHitEvent event, Projectile thrown) {
        thrown.remove();
        onThrownHit(event, thrown);
        thrown.getWorld().playSound(Sounds.getThrowableExplosionSound(this));
    }

    protected void onDoThrow(PlayerInteractEvent event, ItemStack heldItem, Projectile thrown) {
    }

    public void onThrownHit(ProjectileHitEvent event, Projectile thrown) {
    }
}
