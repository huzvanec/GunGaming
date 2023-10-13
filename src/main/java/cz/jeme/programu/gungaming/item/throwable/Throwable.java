package cz.jeme.programu.gungaming.item.throwable;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.util.Sounds;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public abstract class Throwable extends CustomItem {
    public abstract int getThrowCooldown();

    public abstract double getDamage();

    public Throwable() {
        Namespace.THROWABLE.set(item, getName());
    }

    @Override
    public final @NotNull Material getMaterial() {
        return Material.SNOWBALL;
    }

    public final void doThrow(@NotNull PlayerInteractEvent event, @NotNull ItemStack heldItem) {
        Player player = event.getPlayer();

        boolean isCreative = player.getGameMode() == GameMode.CREATIVE;
        if (!isCreative) heldItem.setAmount(heldItem.getAmount() - 1);

        Vector thrownVector = player.getLocation().getDirection();

        ThrowableProjectile thrown = player.launchProjectile(Snowball.class, thrownVector);
        Namespace.THROWN.set(thrown, getName());
        Namespace.THROWN_DAMAGE.set(thrown, getDamage());

        player.getWorld().playSound(Sounds.getThrowableThrowSound(this), player);

        onThrow(event, heldItem, thrown);
    }

    public final void thrownHit(@NotNull ProjectileHitEvent event, @NotNull Projectile thrown) {
        thrown.remove();
        onThrownHit(event, thrown);
        Location location = thrown.getLocation();
        thrown.getWorld().playSound(Sounds.getThrowableExplosionSound(this), location.getX(), location.getY(), location.getZ());
    }

    protected void onThrow(@NotNull PlayerInteractEvent event, @NotNull ItemStack heldItem, @NotNull Projectile thrown) {
    }

    protected void onThrownHit(@NotNull ProjectileHitEvent event, @NotNull Projectile thrown) {
    }
}