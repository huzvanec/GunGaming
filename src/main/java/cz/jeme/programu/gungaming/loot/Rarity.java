package cz.jeme.programu.gungaming.loot;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public enum Rarity {
    COMMON("common", "<white>"),
    UNCOMMON("uncommon", "<dark_green>"),
    RARE("rare", "<blue>"),
    EPIC("epic", "<dark_purple>"),
    LEGENDARY("legendary", "<#d58e00>"),
    UNOBTAINABLE("unobtainable", "<dark_red>");

    private final @NotNull Component color;
    private final @NotNull Key key;

    Rarity(final @KeyPattern.Value @NotNull String key, final @NotNull String color) {
        this.color = Components.of(color);
        this.key = GunGaming.namespaced(key);
    }

    public @NotNull Component color() {
        return color;
    }

    public @NotNull Key key() {
        return key;
    }

    private static final @NotNull Map<String, Rarity> REGISTRY = new HashMap<>();

    static {
        for (final Rarity rarity : values()) {
            REGISTRY.put(rarity.key().asString(), rarity);
        }
    }


    public static @NotNull Rarity of(final @NotNull String keyStr) {
        final Rarity rarity = REGISTRY.get(keyStr);
        if (rarity == null)
            throw new IllegalArgumentException("No registered Rarity of key \""
                                               + keyStr + "\" exists!");
        return rarity;
    }
}
