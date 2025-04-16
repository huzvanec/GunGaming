package cz.jeme.gungaming.loot;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.util.Components;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

@NullMarked
public enum Rarity {
    COMMON("common", "<white>"),
    UNCOMMON("uncommon", "<#22DD22>"),
    RARE("rare", "<#22DDDD>"),
    EPIC("epic", "<#DD22DD>"),
    LEGENDARY("legendary", "<#DDDD22>"),
    UNOBTAINABLE("unobtainable", "<#DD2222>");

    private final Component color;
    private final Key key;

    Rarity(final @KeyPattern.Value String key, final String color) {
        this.color = Components.of(color);
        this.key = GunGaming.key(key);
    }

    public Component color() {
        return color;
    }

    public Key key() {
        return key;
    }

    private static final Map<String, Rarity> REGISTRY = new HashMap<>();

    static {
        for (final Rarity rarity : values()) {
            REGISTRY.put(rarity.key().asString(), rarity);
        }
    }


    public static Rarity of(final String keyStr) {
        final Rarity rarity = REGISTRY.get(keyStr);
        if (rarity == null)
            throw new IllegalArgumentException("No registered Rarity of key \""
                                               + keyStr + "\" exists!");
        return rarity;
    }
}
