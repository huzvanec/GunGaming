package cz.jeme.programu.gungaming.util.item;

import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.item.throwable.Throwable;
import cz.jeme.programu.gungaming.util.Lores;
import cz.jeme.programu.gungaming.util.Messages;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Throwables {

    public static Map<String, Throwable> throwables = new HashMap<>();

    private Throwables() {
        // Static class cannot be initialized
    }

    public static boolean isThrowable(ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        return Namespaces.THROWABLE.has(meta);
    }

    public static boolean isThrown(Projectile projectile) {
        return Namespaces.THROWN.has(projectile);
    }

    public static Throwable getThrowable(String name) {
        return throwables.get(name);
    }

    public static Throwable getThrowable(Component component) {
        return getThrowable(Messages.strip(component));
    }

    public static Throwable getThrowable(ItemStack item) {
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        return getThrowable(meta.displayName());
    }

    public static void register(Throwable throwable) {
        throwables.put(throwable.name, throwable);
        Lores.update(throwable.item);
    }

    public static void registered() {
        throwables = Collections.unmodifiableMap(throwables);
    }
}
