package cz.jeme.programu.gungaming.util.item;

import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.util.Lores;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.Namespaces;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Guns {

    public static Map<String, Gun> guns = new HashMap<>();

    private Guns() {
    }

    public static boolean isGun(ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        return Namespaces.GUN.has(meta);
    }

    public static Gun getGun(String name) {
        return guns.get(name);
    }

    public static Gun getGun(Component component) {
        return getGun(Messages.strip(component));
    }

    public static Gun getGun(ItemStack item) {
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        return getGun(meta.displayName());
    }

    public static void register(Gun gun) {
        guns.put(gun.name, gun);
        Lores.update(gun.item);
    }

    public static void setUnmodifiable() {
        guns = Collections.unmodifiableMap(guns);
    }
}
