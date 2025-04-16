package cz.jeme.gungaming.item.block;

import cz.jeme.gungaming.CustomElement;
import cz.jeme.gungaming.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@NullMarked
public abstract class CustomBlock extends CustomItem {
    static final Map<Material, CustomBlock> BLOCK_REGISTRY = new HashMap<>();

    protected CustomBlock() {
        if (!material.isBlock())
            throw new IllegalArgumentException("A CustomBlock must have a block material!");
        if (BLOCK_REGISTRY.containsKey(material))
            throw new IllegalArgumentException("A CustomBlock must have a unique material!");
        BLOCK_REGISTRY.put(material, this);
        addTags("block");
    }

    @Override
    protected final String provideType() {
        return "block";
    }

    @Override
    protected boolean provideUsesDefaultModel() {
        return true;
    }

    protected void onPlace(final BlockPlaceEvent event) {
    }

    // static accessors

    public static CustomBlock of(final String keyStr) {
        return CustomElement.of(keyStr, CustomBlock.class);
    }

    public static CustomBlock of(final ItemStack item) {
        return CustomItem.of(item, CustomBlock.class);
    }

    public static boolean is(final String keyStr) {
        return CustomElement.is(keyStr, CustomBlock.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, CustomBlock.class);
    }
}
