package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.data.Data;
import cz.jeme.programu.gungaming.item.attachment.impl.Silencer;
import cz.jeme.programu.gungaming.util.Components;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

public final class PlaceholderHelper {
    public static final @NotNull Data<Byte, Boolean> PLACEHOLDER_DATA = Data.ofBoolean(GunGaming.namespaced("placeholder"));
    public static final @NotNull Data<Byte, Boolean> DISABLED_DATA = Data.ofBoolean(GunGaming.namespaced("disabled"));

    private static final @NotNull ItemStack DISABLED = ItemStack.of(Material.WHITE_STAINED_GLASS_PANE);

    static {
        DISABLED.editMeta(meta -> {
            DISABLED_DATA.write(meta, true);
            meta.setCustomModelData(7);
            meta.setMaxStackSize(1);
        });
    }

    private PlaceholderHelper() {
        throw new AssertionError();
    }

    public static @NotNull ItemStack placeholder() {
        return ItemStack.of(Material.WHITE_STAINED_GLASS_PANE);
    }

    public static @NotNull ItemStack placeholder(final @NotNull Consumer<? super ItemMeta> consumer) {
        final ItemStack item = placeholder();
        item.editMeta(meta -> {
            PLACEHOLDER_DATA.write(meta, true);
            meta.setMaxStackSize(1);
            consumer.accept(meta);
        });
        return item;
    }

    private static final @NotNull Map<Class<? extends Attachment>, String> DISABLED_NAMES = Map.of(
            Silencer.class, "Silencer",
            Grip.class, "Grip",
            Scope.class, "Scope",
            Magazine.class, "Magazine",
            Stock.class, "Stock"
    );

    public static @NotNull ItemStack disabled(final @NotNull Class<? extends Attachment> clazz) {
        final ItemStack disabled = DISABLED.clone();
        final String name = DISABLED_NAMES.get(clazz);
        if (name == null)
            throw new RuntimeException("Unknown attachment category: " + clazz.getCanonicalName());
        disabled.editMeta(meta -> meta.displayName(
                Components.of("<!i><red>%s is inapplicable for this weapon!".formatted(name))
        ));
        return disabled;
    }
}
