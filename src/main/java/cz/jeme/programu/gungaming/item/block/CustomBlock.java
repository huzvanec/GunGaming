package cz.jeme.programu.gungaming.item.block;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class CustomBlock extends CustomItem {
    static final @NotNull Map<Material, CustomBlock> BLOCK_REGISTRY = new HashMap<>();

    protected CustomBlock() {
        if (!material.isBlock())
            throw new IllegalArgumentException("A CustomBlock must have a block material!");
        if (BLOCK_REGISTRY.containsKey(material))
            throw new IllegalArgumentException("A CustomBlock must have a unique material!");
        BLOCK_REGISTRY.put(material, this);
        addTags("block");
    }

    @Override
    protected final @NotNull String provideType() {
        return "block";
    }

    protected void onPlace(final @NotNull BlockPlaceEvent event) {
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
