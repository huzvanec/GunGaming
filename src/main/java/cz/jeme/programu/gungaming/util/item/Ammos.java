package cz.jeme.programu.gungaming.util.item;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.util.Lores;
import cz.jeme.programu.gungaming.util.Namespaces;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Ammos {
    public static Map<String, Ammo> ammos = new HashMap<>();

    private Ammos() {
    }

    public static Ammo getAmmo(String name) {
        return ammos.get(name);
    }

    public static Ammo getAmmo(ItemStack item) {
        return getAmmo(Guns.getGun(item));
    }

    public static Ammo getAmmo(Gun gun) {
        return getAmmo(gun.ammoName);
    }

    public static void register(Ammo ammo) {
        ammos.put(ammo.name, ammo);
    }

    public static void setUnmodifiable() {
        ammos = Collections.unmodifiableMap(ammos);
    }

    public static void add(ItemStack item, int count) {
        set(item, (int) Namespaces.CURRENT_GUN_AMMO.get(item) + count);
    }

    public static void remove(ItemStack item, int count) {
        add(item, count * -1);
    }

    public static void set(ItemStack item, int count) {
        Namespaces.CURRENT_GUN_AMMO.set(item, count);
        Damageable meta = (Damageable) item.getItemMeta();

        int currentAmmo = Namespaces.CURRENT_GUN_AMMO.get(item);
        int maxAmmo = Namespaces.MAX_GUN_AMMO.get(item);

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