package cz.jeme.programu.gungaming.util.registry;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.util.Lores;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Guns {

    public static @NotNull Map<String, Gun> guns = new HashMap<>();

    private Guns() {
        throw new AssertionError();
    }

    public static boolean isGun(@Nullable ItemStack item) {
        return Namespace.GUN.has(item);
    }

    public static @Nullable Gun getGun(@NotNull String name) {
        return guns.get(name);
    }

    public static @NotNull Gun getGun(@NotNull ItemStack item) {
        String name = Namespace.GUN.get(item);
        assert name != null : "This item is not a Gun!";
        Gun gun = getGun(name);
        assert gun != null : "This item has a Gun tag, that doesn't represent any registered Gun!";
        return gun;
    }

    public static void register(@NotNull Gun gun) {
        guns.put(gun.getName(), gun);
        Lores.update(gun.getItem());
    }

    public static void registered() {
        guns = Collections.unmodifiableMap(guns);
    }
}
