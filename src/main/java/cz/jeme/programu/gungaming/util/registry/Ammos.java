package cz.jeme.programu.gungaming.util.registry;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.util.Lores;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Ammos {
    public static @NotNull Map<String, Ammo> ammos = new HashMap<>();
    private static @NotNull Map<Class<? extends Ammo>, Ammo> ammoTypes = new HashMap<>();

    private Ammos() {
        // Static class cannot be initialized
    }

    public static @Nullable Ammo getAmmo(@NotNull String name) {
        return ammos.get(name);
    }

    public static @NotNull Ammo getAmmo(@NotNull ItemStack item) {
        String name = Namespace.AMMO.get(item);
        assert name != null : "This item is not an Ammo!";
        Ammo ammo = getAmmo(name);
        assert ammo != null : "This item has an Ammo tag, that doesn't represent any registered Ammo!";
        return ammo;
    }

    public static @NotNull Ammo getAmmo(@NotNull Class<? extends Ammo> ammoType) {
        Ammo ammo = ammoTypes.get(ammoType);
        assert ammo != null : "The Ammo class given doesn't represent any registered Ammo!";
        return ammo;
    }

    public static void register(@NotNull Ammo ammo) {
        ammos.put(ammo.name, ammo);
        ammoTypes.put(ammo.getClass(), ammo);
    }

    public static void registered() {
        ammos = Collections.unmodifiableMap(ammos);
        ammoTypes = Collections.unmodifiableMap(ammoTypes);
    }

    public static boolean isBullet(@Nullable Projectile projectile) {
        return Namespace.BULLET.has(projectile);
    }

    public static void add(@NotNull ItemStack item, int count) {
        Integer currentAmmo = Namespace.GUN_AMMO_CURRENT.get(item);
        assert currentAmmo != null : "Current ammo is null";
        set(item, currentAmmo + count);
    }

    public static void remove(@NotNull ItemStack item, int count) {
        add(item, count * -1);
    }

    public static void set(@NotNull ItemStack item, int count) {
        Namespace.GUN_AMMO_CURRENT.set(item, count);
        Damageable meta = (Damageable) item.getItemMeta();

        Integer currentAmmo = Namespace.GUN_AMMO_CURRENT.get(item);
        Integer maxAmmo = Namespace.GUN_AMMO_MAX.get(item);
        assert currentAmmo != null : "Current ammo is null!";
        assert maxAmmo != null : "Max ammo is null!";

        int maxDamage = item.getType().getMaxDurability();
        int damage;
        if (currentAmmo == 0) {
            damage = maxDamage;
        } else {
            damage = Math.round(maxDamage - maxDamage / (maxAmmo / (float) currentAmmo));
        }
        meta.setDamage(damage);
        item.setItemMeta(meta);
        Lores.update(item);
    }
}