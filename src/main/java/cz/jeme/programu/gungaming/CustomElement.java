package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.data.Data;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public abstract class CustomElement {
    public static final @NotNull Data<String, String> KEY_DATA = Data.ofString(GunGaming.namespaced("element_key"));

    protected final @NotNull Key key = GunGaming.namespaced(provideKey());
    protected final @NotNull Rarity rarity = provideRarity();
    protected final @NotNull Component name = rarity.color().append(provideName());
    protected final @NotNull Component strippedName = Component.text(Components.strip(name));

    // providers

    protected abstract @KeyPattern.Value @NotNull String provideKey();

    protected abstract @NotNull Rarity provideRarity();

    protected abstract @NotNull Component provideName();

    // getters

    public final @NotNull Key key() {
        return key;
    }

    public final @NotNull Rarity rarity() {
        return rarity;
    }

    public final @NotNull Component name() {
        return name;
    }

    public final @NotNull Component strippedName() {
        return strippedName;
    }

    // static accessors

    public static <T extends CustomElement> @NotNull T of(final @NotNull Class<T> elementClass) {
        return ElementManager.INSTANCE.getElement(elementClass)
                .orElseThrow(() -> new IllegalArgumentException("No registered CustomElement of class \""
                                                                + elementClass.getCanonicalName() + "\" exists!"));
    }


    public static @NotNull CustomElement of(final @NotNull String keyStr) {
        return ElementManager.INSTANCE.getElement(keyStr)
                .orElseThrow(() -> new IllegalArgumentException("No registered CustomElement of key \""
                                                                + keyStr + "\" exists!"));
    }


    public static boolean is(final @NotNull Class<? extends CustomElement> elementClass) {
        return ElementManager.INSTANCE.existsElement(elementClass);
    }

    public static boolean is(final @NotNull String keyStr) {
        return ElementManager.INSTANCE.existsElement(keyStr);
    }

    public static <T extends CustomElement> @NotNull T of(final @NotNull String keyStr, final @NotNull Class<T> elementClass) {
        return ElementManager.INSTANCE.getElement(keyStr, elementClass)
                .orElseThrow(() -> new IllegalArgumentException("No registered CustomElement of class \""
                                                                + elementClass.getCanonicalName() + "\" and key \""
                                                                + keyStr + "\" exists!"));
    }

    public static boolean is(final @NotNull String keyStr, final @NotNull Class<? extends CustomElement> elementClass) {
        return ElementManager.INSTANCE.existsElement(keyStr, elementClass);
    }
}
