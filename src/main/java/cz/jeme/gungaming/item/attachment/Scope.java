package cz.jeme.gungaming.item.attachment;

import cz.jeme.gungaming.CustomElement;
import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.persistence.PersistentData;
import cz.jeme.gungaming.util.Components;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class Scope extends Attachment {
    public static final PersistentData<String, String> GUN_SCOPE_KEY_DATA = PersistentData.ofString(GunGaming.key("gun_scope_key"));
    private static final ItemStack PLACEHOLDER = PlaceholderHelper.placeholder(
            GunGaming.key("scope_placeholder"),
            meta -> meta.displayName(Components.of("<!i><gray>Scope"))
    );

    public static ItemStack placeholder(final ItemStack gunItem) {
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


    public static Scope of(final String keyStr) {
        return CustomElement.of(keyStr, Scope.class);
    }

    public static Scope of(final ItemStack item) {
        return CustomItem.of(item, Scope.class);
    }

    public static boolean is(final String keyStr) {
        return CustomElement.is(keyStr, Scope.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Scope.class);
    }
}
