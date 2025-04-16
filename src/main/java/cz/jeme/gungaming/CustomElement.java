package cz.jeme.gungaming;

import cz.jeme.gungaming.loot.Rarity;
import cz.jeme.gungaming.persistence.PersistentData;
import cz.jeme.gungaming.util.Components;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class CustomElement {
    public static final PersistentData<String, String> KEY_DATA = PersistentData.ofString(GunGaming.key("element_key"));

    protected final Key key = GunGaming.key(provideKey());
    protected final Rarity rarity = provideRarity();
    protected final Component name = rarity.color().append(provideName());
    protected final Component strippedName = Component.text(Components.strip(name));

    // providers

    protected abstract @KeyPattern.Value String provideKey();

    protected abstract Rarity provideRarity();

    protected abstract Component provideName();

    // getters

    public final Key key() {
        return key;
    }

    public final Rarity rarity() {
        return rarity;
    }

    public final Component name() {
        return name;
    }

    public final Component strippedName() {
        return strippedName;
    }

    // static accessors

    public static <T extends CustomElement> T of(final Class<T> elementClass) {
        return ElementManager.INSTANCE.getElement(elementClass)
                .orElseThrow(() -> new IllegalArgumentException("No registered CustomElement of class \""
                                                                + elementClass.getCanonicalName() + "\" exists!"));
    }


    public static CustomElement of(final String keyStr) {
        return ElementManager.INSTANCE.getElement(keyStr)
                .orElseThrow(() -> new IllegalArgumentException("No registered CustomElement of key \""
                                                                + keyStr + "\" exists!"));
    }


    public static boolean is(final Class<? extends CustomElement> elementClass) {
        return ElementManager.INSTANCE.existsElement(elementClass);
    }

    public static boolean is(final String keyStr) {
        return ElementManager.INSTANCE.existsElement(keyStr);
    }

    public static <T extends CustomElement> T of(final String keyStr, final Class<T> elementClass) {
        return ElementManager.INSTANCE.getElement(keyStr, elementClass)
                .orElseThrow(() -> new IllegalArgumentException("No registered CustomElement of class \""
                                                                + elementClass.getCanonicalName() + "\" and key \""
                                                                + keyStr + "\" exists!"));
    }

    public static boolean is(final String keyStr, final Class<? extends CustomElement> elementClass) {
        return ElementManager.INSTANCE.existsElement(keyStr, elementClass);
    }
}
