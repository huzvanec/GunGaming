package cz.jeme.programu.gungaming.item.throwable;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.data.Data;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.util.Lores;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class Throwable extends CustomItem {
    public static final @NotNull Data<Integer, Integer> THROW_COOLDOWN_DATA = Data.ofInteger(GunGaming.namespaced("throwable_throw_cooldown"));
    public static final @NotNull Data<Double, Double> MAX_DAMAGE_DATA = Data.ofDouble(GunGaming.namespaced("throwable_max_damage"));

    protected final int throwCooldown = provideThrowCooldown();
    protected final double maxDamage = provideMaxDamage();
    protected final double minDamage = provideMinDamage();
    /**
     * Points of damage (half hearts) by which the maximum damage is reduced
     * each block the entity is away from the center of the explosion.
     */
    protected final double damageDistanceRatio = provideDamageDistanceRatio();

    protected Throwable() {
        if (throwCooldown < 0)
            throw new IllegalArgumentException("Throw cooldown cannot be negative!");
        if (maxDamage < 0)
            throw new IllegalArgumentException("Max damage cannot be negative!");
        if (minDamage < 0)
            throw new IllegalArgumentException("Min damage cannot be negative!");
        if (minDamage > maxDamage)
            throw new IllegalArgumentException("Min damage cannot be greater than max damage!");
        if (damageDistanceRatio < 0)
            throw new IllegalArgumentException("Damage distance ratio cannot be negative!");

        addTags("weapon", "throwable");

        item.editMeta(meta -> {
            THROW_COOLDOWN_DATA.write(meta, throwCooldown);
            MAX_DAMAGE_DATA.write(meta, maxDamage);
        });
    }

    // providers

    protected abstract int provideThrowCooldown();

    protected abstract double provideMaxDamage();

    protected double provideMinDamage() {
        return maxDamage / 4;
    }

    protected double provideDamageDistanceRatio() {
        return 2;
    }

    // getters

    public final int throwCooldown() {
        return throwCooldown;
    }

    public final double maxDamage() {
        return maxDamage;
    }

    public final double damageDistanceRatio() {
        return damageDistanceRatio;
    }

    // throwing


    @Override
    protected final void onUse(final @NotNull PlayerInteractEvent event) {
        event.setCancelled(true);
        doThrow(event);
    }

    private void doThrow(final @NotNull PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();
        assert item != null;

        final Material material = item.getType();
        if (player.hasCooldown(material)) return;
        final int throwCooldown = THROW_COOLDOWN_DATA.require(item);
        player.setCooldown(material, throwCooldown);

        final Vector throwVector = player.getLocation().getDirection();

        player.launchProjectile(
                Snowball.class,
                throwVector,
                snowball -> {
                    ThrownHelper.THROWABLE_KEY_DATA.write(snowball, key.asString());
                    ThrownHelper.MAX_DAMAGE_DATA.write(snowball, MAX_DAMAGE_DATA.require(item));
                    snowball.setItem(item);
                    onThrow(event, snowball);
                }
        );

        if (player.getGameMode() != GameMode.CREATIVE) item.setAmount(item.getAmount() - 1);

        player.getWorld().playSound(throwSound(item), player);
    }

    protected void onThrow(final @NotNull PlayerInteractEvent event, final @NotNull Snowball thrown) {
    }

    final void thrownHit(final @NotNull ProjectileHitEvent event, final @NotNull Snowball thrown) {
        final Location location = thrown.getLocation();
        thrown.getWorld().playSound(hitSound(thrown), location.getX(), location.getY(), location.getZ());
        onThrownHit(event, thrown);
        thrown.remove();
    }

    protected void onThrownHit(final @NotNull ProjectileHitEvent event, final @NotNull Snowball thrown) {
    }

    // sounds

    protected final @NotNull Key throwSoundKey = GunGaming.namespaced("item." + key.value() + ".throw");
    protected final @NotNull Key hitSoundKey = GunGaming.namespaced("entity." + key.value() + ".hit");

    protected final @NotNull Sound throwSound = Sound.sound(throwSoundKey, Sound.Source.PLAYER, 1.9F, 1);
    protected final @NotNull Sound hitSound = Sound.sound(hitSoundKey, Sound.Source.HOSTILE, 9.4F, 1);

    public @NotNull Sound throwSound(final @NotNull ItemStack item) {
        return throwSound;
    }

    public @NotNull Sound hitSound(final @NotNull Snowball thrown) {
        return hitSound;
    }

    // override stuff

    @Override
    protected final @NotNull Material provideMaterial() {
        return Material.SNOWBALL;
    }

    @Override
    protected final @NotNull String provideType() {
        return "throwable";
    }

    @Override
    protected @NotNull List<String> update(final @NotNull ItemStack item) {
        return List.of(Lores.loreStat("Damage", Lores.STATS_FORMATTER.format(maxDamage)));
    }

    // static accessors

    public static @NotNull Throwable of(final @NotNull String keyStr) {
        return CustomElement.of(keyStr, Throwable.class);
    }

    public static @NotNull Throwable of(final @NotNull ItemStack item) {
        return CustomItem.of(item, Throwable.class);
    }

    public static boolean is(final @NotNull String keyStr) {
        return CustomElement.is(keyStr, Throwable.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Throwable.class);
    }
}