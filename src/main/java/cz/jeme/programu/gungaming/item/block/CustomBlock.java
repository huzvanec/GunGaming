package cz.jeme.programu.gungaming.item.block;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.item.CustomItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CustomBlock extends CustomItem {
    protected CustomBlock() {
        if (!material.isBlock())
            throw new IllegalArgumentException("A CustomBlock must have a block material!");
        addTags("block");
    }

    @Override
    protected final @NotNull String provideType() {
        return "block";
    }

    // static accessors

    public static @NotNull CustomBlock of(final @NotNull String keyStr) {
        return CustomElement.of(keyStr, CustomBlock.class);
    }

    public static @NotNull CustomBlock of(final @NotNull ItemStack item) {
        return CustomItem.of(item, CustomBlock.class);
    }

    public static boolean is(final @NotNull String keyStr) {
        return CustomElement.is(keyStr, CustomBlock.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, CustomBlock.class);
    }
}
