package cz.jeme.programu.gungaming.util.registry;

import cz.jeme.programu.gungaming.loot.crate.Crate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Crates {

    public static @NotNull Map<String, Crate> crates = new HashMap<>();

    private Crates() {
        // Static class cannot be initialized
    }

    public static @Nullable Crate getCrate(@NotNull String name) {
        return crates.get(name);
    }

    public static void register(@NotNull Crate crate) {
        crates.put(crate.getName(), crate);
    }

    public static void registered() {
        crates = Collections.unmodifiableMap(crates);
    }
}
