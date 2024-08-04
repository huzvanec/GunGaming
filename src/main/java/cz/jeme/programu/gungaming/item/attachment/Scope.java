package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.data.Data;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.util.Components;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Scope extends Attachment {
    public static final @NotNull Data<String, String> GUN_SCOPE_KEY_DATA = Data.ofString(GunGaming.namespaced("gun_scope_key"));
    private static final @NotNull ItemStack PLACEHOLDER = PlaceholderHelper.placeholder(meta -> {
        meta.displayName(Components.of("<!i><gray>Scope"));
        meta.setCustomModelData(3);
    });

    public static @NotNull ItemStack placeholder(final @NotNull ItemStack gunItem) {
        return PLACEHOLDER.clone();
    }

    protected Scope() {
        addTags("scope");
    }

    protected final double zoom = provideZoom();

    protected abstract double provideZoom();

    public final double zoom() {
        return zoom;
    }


    public static @NotNull Scope of(final @NotNull String keyStr) {
        return CustomElement.of(keyStr, Scope.class);
    }

    public static @NotNull Scope of(final @NotNull ItemStack item) {
        return CustomItem.of(item, Scope.class);
    }

    public static boolean is(final @NotNull String keyStr) {
        return CustomElement.is(keyStr, Scope.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Scope.class);
    }
}
