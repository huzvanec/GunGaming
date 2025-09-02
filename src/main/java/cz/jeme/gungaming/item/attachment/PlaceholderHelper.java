package cz.jeme.gungaming.item.attachment;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.attachment.impl.Silencer;
import cz.jeme.gungaming.persistence.PersistentData;
import cz.jeme.gungaming.util.Components;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.function.Consumer;

@NullMarked
public final class PlaceholderHelper {
    public static final PersistentData<Byte, Boolean> PLACEHOLDER_DATA = PersistentData.ofBoolean(GunGaming.key("placeholder"));
    public static final PersistentData<Byte, Boolean> DISABLED_DATA = PersistentData.ofBoolean(GunGaming.key("disabled"));

    private static final ItemStack DISABLED = ItemStack.of(Material.WHITE_STAINED_GLASS_PANE);

    static {
        DISABLED.editMeta(meta -> DISABLED_DATA.write(meta, true));
        //noinspection UnstableApiUsage
        DISABLED.setData(DataComponentTypes.ITEM_MODEL, GunGaming.key("disabled_attachment"));
    }

    private PlaceholderHelper() {
        throw new AssertionError();
    }

    public static ItemStack placeholder() {
        return ItemStack.of(Material.WHITE_STAINED_GLASS_PANE);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static ItemStack placeholder(final Key key, final Consumer<? super ItemMeta> consumer) {
        final ItemStack item = placeholder();
        item.editMeta(meta -> {
            PLACEHOLDER_DATA.write(meta, true);
            meta.setMaxStackSize(1);
            consumer.accept(meta);
        });
        item.setData(DataComponentTypes.ITEM_MODEL, key);
        return item;
    }

    private static final Map<Class<? extends Attachment>, String> DISABLED_NAMES = Map.of(
            Silencer.class, "silencer",
            Grip.class, "grip",
            Scope.class, "scope",
            Magazine.class, "magazine",
            Stock.class, "stock"
    );

    public static ItemStack disabled(final Class<? extends Attachment> clazz) {
        final ItemStack disabled = DISABLED.clone();
        final String name = DISABLED_NAMES.get(clazz);
        if (name == null)
            throw new RuntimeException("Unknown attachment category: " + clazz.getCanonicalName());
        disabled.editMeta(meta -> meta.displayName(
                Components.of("<!i><red>This weapon cannot use a %s attachment!".formatted(name))
        ));
        return disabled;
    }
}
