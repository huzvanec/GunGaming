package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.config.GenerationConfig;
import cz.jeme.programu.gungaming.data.Data;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.Weapon;
import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.attachment.Grip;
import cz.jeme.programu.gungaming.item.attachment.Magazine;
import cz.jeme.programu.gungaming.item.attachment.Scope;
import cz.jeme.programu.gungaming.item.attachment.ZoomManager;
import cz.jeme.programu.gungaming.item.attachment.impl.Silencer;
import cz.jeme.programu.gungaming.loot.crate.Crate;
import cz.jeme.programu.gungaming.util.*;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Gun extends Weapon {
    public static final @NotNull Data<Integer, Integer> MAX_AMMO_DATA = Data.ofInteger(GunGaming.namespaced("gun_max_ammo"));
    public static final @NotNull Data<Integer, Integer> SHOOT_COOLDOWN_DATA = Data.ofInteger(GunGaming.namespaced("gun_shoot_cooldown"));
    public static final @NotNull Data<Integer, Integer> RELOAD_DURATION_DATA = Data.ofInteger(GunGaming.namespaced("gun_reload_duration"));
    public static final @NotNull Data<Double, Double> DAMAGE_DATA = Data.ofDouble(GunGaming.namespaced("gun_damage"));
    public static final @NotNull Data<Double, Double> BULLET_VELOCITY_DATA = Data.ofDouble(GunGaming.namespaced("gun_bullet_velocity"));
    public static final @NotNull Data<Double, Double> RECOIL_DATA = Data.ofDouble(GunGaming.namespaced("gun_recoil"));
    public static final @NotNull Data<Double, Double> INACCURACY_DATA = Data.ofDouble(GunGaming.namespaced("gun_inaccuracy"));
    public static final @NotNull Data<Integer, Integer> CURRENT_AMMO_DATA = Data.ofInteger(GunGaming.namespaced("gun_current_ammo"));

    private static final double RECOIL_Y_MULTIPLIER = .28;

    static final @NotNull List<ItemStack> PROJECTILES = List.of(ItemStack.of(Material.ARROW));

    protected final int maxAmmo = provideMaxAmmo();
    protected final int shootCooldown = provideShootCooldown();
    protected final int reloadDuration = provideReloadDuration();
    protected final double damage = provideDamage();
    protected final double bulletVelocity = provideBulletVelocity();
    protected final double recoil = provideRecoil();
    protected final double inaccuracy = provideInaccuracy();
    protected final int bulletsPerShot = provideBulletsPerShot();
    protected final int bulletCooldown = provideBulletCooldown();
    protected final boolean magazineless = provideMagazineless();
    protected final @NotNull Class<? extends AbstractArrow> arrowType = provideArrowType();
    protected final @NotNull Class<? extends Ammo> ammoType = provideAmmoType();
    protected final @NotNull Ammo ammo;

    protected Gun() {
        if (maxAmmo < 1)
            throw new IllegalArgumentException("Max ammo must be positive!");
        if (shootCooldown < 0)
            throw new IllegalArgumentException("Shoot cannot be negative!");
        if (reloadDuration < 0)
            throw new IllegalArgumentException("Reload duration cannot be negative!");
        if (damage < 0)
            throw new IllegalArgumentException("Damage cannot be negative!");
        if (bulletVelocity < 0)
            throw new IllegalArgumentException("Bullet velocity cannot be negative!");
        if (recoil < 0)
            throw new IllegalArgumentException("Recoil cannot be negative!");
        if (inaccuracy < 0)
            throw new IllegalArgumentException("Inaccuracy cannot be negative!");
        if (bulletsPerShot < 1)
            throw new IllegalArgumentException("Bullets per shot must be positive!");
        if (bulletCooldown < 0)
            throw new IllegalArgumentException("Bullet cooldown cannot be negative!");

        addTags("gun", "weapon");

        item.editMeta(CrossbowMeta.class, meta -> {
            meta.setChargedProjectiles(PROJECTILES);
            // setup item data
            MAX_AMMO_DATA.write(meta, maxAmmo);
            SHOOT_COOLDOWN_DATA.write(meta, shootCooldown);
            RELOAD_DURATION_DATA.write(meta, reloadDuration);
            DAMAGE_DATA.write(meta, damage);
            BULLET_VELOCITY_DATA.write(meta, bulletVelocity);
            RECOIL_DATA.write(meta, recoil);
            INACCURACY_DATA.write(meta, inaccuracy);
            CURRENT_AMMO_DATA.write(meta, 0);
        });

        ammo = CustomItem.of(ammoType);
    }

    // providers

    protected @NotNull Class<? extends AbstractArrow> provideArrowType() {
        return Arrow.class;
    }

    protected abstract int provideMaxAmmo();

    protected abstract int provideShootCooldown();

    protected abstract int provideReloadDuration();

    protected abstract double provideDamage();

    protected abstract double provideBulletVelocity();

    protected abstract double provideRecoil();

    protected abstract double provideInaccuracy();

    protected int provideBulletsPerShot() {
        return 1;
    }

    protected int provideBulletCooldown() {
        return 1;
    }

    protected boolean provideMagazineless() {
        return false;
    }

    protected abstract @NotNull Class<? extends Ammo> provideAmmoType();

    // getters

    public final @NotNull Class<? extends AbstractArrow> arrowType() {
        return arrowType;
    }

    public final int maxAmmo() {
        return maxAmmo;
    }

    public final int shootCooldown() {
        return shootCooldown;
    }

    public final int reloadDuration() {
        return reloadDuration;
    }

    public final double damage() {
        return damage;
    }

    public final double bulletVelocity() {
        return bulletVelocity;
    }

    public final double recoil() {
        return recoil;
    }

    public final double inaccuracy() {
        return inaccuracy;
    }

    public final int bulletsPerShot() {
        return bulletsPerShot;
    }

    public final int bulletCooldown() {
        return bulletCooldown;
    }

    public final @NotNull Class<? extends Ammo> ammoType() {
        return ammoType;
    }

    public final @NotNull Ammo ammo() {
        return ammo;
    }

    public final boolean magazineless() {
        return magazineless;
    }

    public final boolean shotgun() {
        return bulletCooldown == 0 && bulletsPerShot > 1;
    }

    // shooting

    @Override
    protected final void onUse(final @NotNull PlayerInteractEvent event) {
        event.setCancelled(true);
        shoot(event);
    }

    private void shoot(final @NotNull PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();
        assert item != null;
        final int currentAmmo = CURRENT_AMMO_DATA.require(item);
        if (player.hasCooldown(item.getType()))
            if (ReloadManager.INSTANCE.isReloading(player) && currentAmmo > 0)
                ReloadManager.INSTANCE.abortReload(player, false);
            else return;
        final boolean creative = player.getGameMode() == GameMode.CREATIVE;
        if (currentAmmo == 0 && !creative) {
            noAmmo(player, item);
            return;
        }
        final int shootCooldown = SHOOT_COOLDOWN_DATA.require(item);
        player.setCooldown(item.getType(), shootCooldown);
        shootRound(event, 1);
    }

    private void noAmmo(final @NotNull Player player, final @NotNull ItemStack item) {
        if (Inventories.count(player.getInventory(), ammo.item()) == 0) {
            player.sendActionBar(Components.of("<red>Out of ammo!"));
            player.getWorld().playSound(outOfAmmoSound(item), player);
        } else {
            player.sendActionBar(Components.of("<red>Press <yellow>F</yellow> to reload!"));
            player.getWorld().playSound(reloadRequiredSound(item), player);
        }
    }


    private void shootRound(final @NotNull PlayerInteractEvent event, final int round) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();
        assert item != null;
        final boolean creative = player.getGameMode() == GameMode.CREATIVE;
        final int currentAmmo = CURRENT_AMMO_DATA.require(item);

        // the player isn't in creative mode && the gun has no ammo
        if (!creative && currentAmmo == 0) {
            noAmmo(player, item);
            return;
        }
        // the player isn't in creative mode && (the gun is not a shotgun || the shotgun is shooting its last bullet)
        if (!creative && (!shotgun() || round == bulletsPerShot))
            Gun.removeAmmo(item, 1);

        if (!shotgun() || round == 1) {
            player.getWorld().playSound(shootSound(item), player);
        }

        final double recoil = RECOIL_DATA.require(item);
        final Vector recoilVector = player.getLocation().getDirection().multiply(recoil);
        recoilVector.setY(recoilVector.getY() * RECOIL_Y_MULTIPLIER);
        player.setVelocity(player.getVelocity().subtract(recoilVector));

        final double bulletVelocity = BULLET_VELOCITY_DATA.require(item);
        final Vector bulletVector = player.getLocation().getDirection().multiply(bulletVelocity);
        final double inaccuracyMultiplier = shotgun() && Grip.GUN_GRIP_KEY_DATA.check(item) ? 4 : 1;
        randomizeVector(bulletVector, INACCURACY_DATA.require(item) * inaccuracyMultiplier);

        player.launchProjectile(
                arrowType,
                bulletVector,
                arrow -> {
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                    arrow.setGravity(false);
                    BulletHelper.GUN_KEY_DATA.write(arrow, key.asString());
                    BulletHelper.DAMAGE_DATA.write(arrow, DAMAGE_DATA.require(item));
                    onShoot(event, arrow);
                }
        );

        if (round >= bulletsPerShot) return;

        final ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        final int hand = player.getInventory().getItemInMainHand().equals(item)
                ? ClientboundAnimatePacket.SWING_MAIN_HAND
                : ClientboundAnimatePacket.SWING_OFF_HAND;
        final Packet<ClientGamePacketListener> packet = new ClientboundAnimatePacket(serverPlayer, hand);
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getUniqueId().equals(player.getUniqueId())) continue;
            Packets.send(onlinePlayer, packet);
        }

        Bukkit.getScheduler().runTaskLater(
                GunGaming.plugin(),
                () -> shootRound(event, round + 1),
                bulletCooldown);
    }

    private static double nextAxis(final double radians) {
        return ThreadLocalRandom.current().nextDouble(-radians, radians);
    }

    private static void randomizeVector(final @NotNull Vector vector, final double inaccuracy) {
        final double rad = Math.toRadians(inaccuracy);
        vector.rotateAroundX(nextAxis(rad));
        vector.rotateAroundY(nextAxis(rad));
        vector.rotateAroundZ(nextAxis(rad));
    }

    protected void onShoot(final @NotNull PlayerInteractEvent event, final @NotNull AbstractArrow bullet) {
    }

    protected void onBulletHit(final @NotNull ProjectileHitEvent event, final @NotNull AbstractArrow bullet) {
    }

    // scope

    @Override
    protected void onLeftClickAir(final @NotNull PlayerInteractEvent event) {
        zoom(event);
    }

    @Override
    protected void onLeftClickBlock(final @NotNull PlayerInteractEvent event) {
        if (event.getPlayer().isSneaking()) zoom(event);
    }

    private void zoom(final @NotNull PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        assert item != null;
        final Player player = event.getPlayer();
        if (!Scope.GUN_SCOPE_KEY_DATA.check(item)) {
            player.sendActionBar(Components.of("<red>No scope equipped!"));
            return;
        }
        event.setCancelled(true);
        final Scope scope = Scope.of(Scope.GUN_SCOPE_KEY_DATA.require(item));
        ZoomManager.INSTANCE.nextZoom(player, scope.zoom());
    }


    // sounds

    protected final @NotNull Key shootSoundKey = GunGaming.namespaced("item." + key.value() + ".shoot");
    protected final @NotNull Key reloadSoundKey = GunGaming.namespaced("item." + key.value() + ".reload");
    protected static final @NotNull Key OUT_OF_AMMO_SOUND_KEY = GunGaming.namespaced("item.gun.out_of_ammo");
    protected static final @NotNull Key RELOAD_REQUIRED_SOUND_KEY = GunGaming.namespaced("item.gun.reload_required");
    protected static final @NotNull Key RELOAD_ABORTED_SOUND_KEY = GunGaming.namespaced("item.gun.reload_aborted");

    protected final @NotNull Sound shootSound = Sound.sound(shootSoundKey, Sound.Source.PLAYER, 6.3F, 1);
    protected final @NotNull Sound reloadSound = Sound.sound(reloadSoundKey, Sound.Source.PLAYER, 2.5F, 1);
    protected static final @NotNull Sound OUT_OF_AMMO_SOUND = Sound.sound(OUT_OF_AMMO_SOUND_KEY, Sound.Source.PLAYER, 2.5F, 1);
    protected static final @NotNull Sound RELOAD_REQUIRED_SOUND = Sound.sound(RELOAD_REQUIRED_SOUND_KEY, Sound.Source.PLAYER, 2.5F, 1);
    protected static final @NotNull Sound RELOAD_ABORTED_SOUND = Sound.sound(RELOAD_ABORTED_SOUND_KEY, Sound.Source.PLAYER, 2.5F, 1);

    public @NotNull Sound shootSound(final @NotNull ItemStack item) {
        return Silencer.GUN_SILENCER_KEY_DATA.read(item)
                .map(key -> Sound.sound(
                                shootSoundKey,
                                Sound.Source.PLAYER,
                                (float) (6.3 * CustomItem.of(Silencer.class).volumeMultiplier()),
                                1
                        )
                )
                .orElse(shootSound);
    }

    public @NotNull Sound reloadSound(final @NotNull ItemStack item) {
        return Magazine.GUN_MAGAZINE_KEY_DATA.read(item)
                .map(key -> Sound.sound(
                                reloadSoundKey,
                                Sound.Source.PLAYER,
                                2.5F,
                                (float) (1 / Magazine.of(key).maxAmmoMultiplier())
                        )
                )
                .orElse(reloadSound);
    }

    public @NotNull Sound outOfAmmoSound(final @NotNull ItemStack item) {
        return OUT_OF_AMMO_SOUND;
    }

    public @NotNull Sound reloadRequiredSound(final @NotNull ItemStack item) {
        return RELOAD_REQUIRED_SOUND;
    }

    public @NotNull Sound reloadAbortedSound(final @NotNull ItemStack item) {
        return RELOAD_ABORTED_SOUND;
    }

    // override stuff

    @Override
    protected @NotNull List<String> update(final @NotNull ItemStack item) {
        // gun damage
        final short maxDamage = item.getType().getMaxDurability();
        final int maxAmmo = MAX_AMMO_DATA.require(item);
        final int currentAmmo = CURRENT_AMMO_DATA.require(item);
        final int itemDamage = currentAmmo == 0
                ? maxDamage
                : Math.round(maxDamage - maxDamage / (maxAmmo / (float) currentAmmo));
        item.editMeta(Damageable.class, meta -> meta.setDamage(itemDamage));

        // lore
        final List<String> lore = new ArrayList<>();
        // ammo type
        final String ammoName = Components.strip(ammo.name());
        lore.add(Lores.loreStat("Ammo Type", ammoName));
        // damage
        final double damage = DAMAGE_DATA.require(item);
        final String damageModification = Silencer.GUN_SILENCER_KEY_DATA.check(item) ? "<red>" : "";
        lore.add(Lores.loreStat("Damage", Lores.STATS_FORMATTER.format(damage), damageModification));
        // scope
        if (Scope.GUN_SCOPE_KEY_DATA.check(item)) {
            final Scope scope = Scope.of(Scope.GUN_SCOPE_KEY_DATA.require(item));
            final double zoom = scope.zoom();
            lore.add(Lores.loreStat("Scope", Lores.STATS_FORMATTER.format(zoom) + "×", "<green>"));
        }
        // ammo
        final String ammoModification = Magazine.GUN_MAGAZINE_KEY_DATA.check(item) ? "<green>" : "";
        lore.add(Lores.loreStat(
                "Ammo",
                currentAmmo + "/" + ammoModification + maxAmmo));
        // info
        lore.add("");
        lore.add("<yellow>[Right click to edit attachments]");
        lore.add("<yellow>[Shift right click to view stats]");
        return lore;
    }

    @Override
    public void generated(final @NotNull ItemStack item, final @NotNull Crate crate) {
        final int maxAmmo = MAX_AMMO_DATA.require(item);
        final double chance = GenerationConfig.GUN_AMMO_PERCENTAGE.get() / 100D;
        final int ammo = RandomUtils.nextChanced(chance, maxAmmo);
        setAmmo(item, ammo);
    }

    @Override
    protected final @NotNull Material provideMaterial() {
        return Material.CROSSBOW;
    }

    @Override
    protected final @NotNull String provideType() {
        return "gun";
    }

    // ammo utils

    public static void setAmmo(final @NotNull ItemStack item, final int ammo) {
        if (ammo < 0)
            throw new IllegalArgumentException("Ammo cannot be negative!");
        final Gun gun = Gun.of(item);
        final int maxAmmo = Gun.MAX_AMMO_DATA.require(item);
        if (ammo > maxAmmo)
            throw new IllegalArgumentException("Ammo cannot be greater than max ammo!");
        Gun.CURRENT_AMMO_DATA.write(item, ammo);
        gun.updateItem(item);
    }

    public static void setMaxAmmo(final @NotNull ItemStack item) {
        setAmmo(item, Gun.MAX_AMMO_DATA.require(item));
    }

    public static void addAmmo(final @NotNull ItemStack item, final int ammo) {
        final int currentAmmo = Gun.CURRENT_AMMO_DATA.require(item);
        setAmmo(item, currentAmmo + ammo);
    }

    public static void removeAmmo(final @NotNull ItemStack item, final int ammo) {
        addAmmo(item, -ammo);
    }

    // static accessors

    public static @NotNull Gun of(final @NotNull String keyStr) {
        return CustomElement.of(keyStr, Gun.class);
    }

    public static @NotNull Gun of(final @NotNull ItemStack item) {
        return CustomItem.of(item, Gun.class);
    }

    public static boolean is(final @NotNull String keyStr) {
        return CustomElement.is(keyStr, Gun.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Gun.class);
    }
}

