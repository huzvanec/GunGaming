package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.data.Data;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CustomElement {
    public static final @NotNull Data<String, String> KEY_DATA = Data.ofString(GunGaming.namespaced("element_key"));

    protected final @NotNull Key key = GunGaming.namespaced(provideKey());

    protected abstract @KeyPattern.Value @NotNull String provideKey();

    public final @NotNull Key key() {
        return key;
    }

    // object

    @Override
    public final boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof final CustomElement that)) return false;

        return key.equals(that.key);
    }

    @Override
    public final int hashCode() {
        return key.hashCode();
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
