package cz.jeme.programu.gungaming.util.registry;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.throwable.Throwable;
import cz.jeme.programu.gungaming.util.Lores;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Throwables {

    public static @NotNull Map<String, Throwable> throwables = new HashMap<>();

    private Throwables() {
        // Static class cannot be initialized
    }

    public static boolean isThrowable(@Nullable ItemStack item) {
        return Namespace.THROWABLE.has(item);
    }

    public static boolean isThrown(@Nullable Projectile projectile) {
        return Namespace.THROWN.has(projectile);
    }

    public static @Nullable Throwable getThrowable(@NotNull String name) {
        return throwables.get(name);
    }

    public static @NotNull Throwable getThrowable(@NotNull ItemStack item) {
        String name = Namespace.THROWABLE.get(item);
        assert name != null : "This item is not a Throwable!";
        Throwable throwable = getThrowable(name);
        assert throwable != null : "This item has a Throwable tag, that doesn't represent any registered Throwable!";
        return throwable;
    }

    public static void register(@NotNull Throwable throwable) {
        throwables.put(throwable.getName(), throwable);
        Lores.update(throwable.getItem());
    }

    public static void registered() {
        throwables = Collections.unmodifiableMap(throwables);
    }
}