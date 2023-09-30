package cz.jeme.programu.gungaming.util.registry;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.misc.Misc;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Miscs {
    public static @NotNull Map<String, Misc> miscs = new HashMap<>();

    private Miscs() {
        // Static class cannot be initialized
    }

    public static @Nullable Misc getMisc(@NotNull String name) {
        return miscs.get(name);
    }

    public static @NotNull Misc getMisc(@NotNull ItemStack item) {
        String name = Namespace.MISC.get(item);
        assert name != null : "This item is not a Misc!";
        Misc misc = getMisc(name);
        assert misc != null : "This item has a Misc tag, that doesn't represent any registered Misc!";
        return misc;
    }

    public static boolean isMisc(@Nullable ItemStack item) {
        return Namespace.MISC.has(item);
    }

    public static void register(@NotNull Misc misc) {
        miscs.put(misc.name, misc);
    }

    public static void registered() {
        miscs = Collections.unmodifiableMap(miscs);
    }
}
