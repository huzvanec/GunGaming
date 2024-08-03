package cz.jeme.programu.gungaming.util;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class Materials {
    private Materials() {
        throw new AssertionError();
    }

    private static final @NotNull Set<Material> OTHER_ACTION_BLOCKS = Set.of(
            Material.BARREL, Material.SMOKER, Material.DISPENSER,
            Material.DROPPER, Material.HOPPER, Material.GRINDSTONE,
            Material.LOOM, Material.STONECUTTER, Material.BREWING_STAND,
            Material.BEACON, Material.REDSTONE, Material.LEVER,
            Material.REPEATER, Material.COMPARATOR, Material.REDSTONE_ORE,
            Material.NOTE_BLOCK, Material.DAYLIGHT_DETECTOR, Material.BELL,
            Material.CRAFTER, Material.CHISELED_BOOKSHELF
    );

    public static boolean isGunBreakable(final @NotNull Material material) {
        return switch (material) {
            case GLASS, GLASS_PANE,
                 WHITE_STAINED_GLASS, WHITE_STAINED_GLASS_PANE,
                 LIGHT_GRAY_STAINED_GLASS, LIGHT_GRAY_STAINED_GLASS_PANE,
                 GRAY_STAINED_GLASS, GRAY_STAINED_GLASS_PANE,
                 BLACK_STAINED_GLASS, BLACK_STAINED_GLASS_PANE,
                 BLUE_STAINED_GLASS, BLUE_STAINED_GLASS_PANE,
                 BROWN_STAINED_GLASS, BROWN_STAINED_GLASS_PANE,
                 CYAN_STAINED_GLASS, CYAN_STAINED_GLASS_PANE,
                 GREEN_STAINED_GLASS, GREEN_STAINED_GLASS_PANE,
                 LIGHT_BLUE_STAINED_GLASS, LIGHT_BLUE_STAINED_GLASS_PANE,
                 LIME_STAINED_GLASS, LIME_STAINED_GLASS_PANE,
                 MAGENTA_STAINED_GLASS, MAGENTA_STAINED_GLASS_PANE,
                 ORANGE_STAINED_GLASS, ORANGE_STAINED_GLASS_PANE,
                 PINK_STAINED_GLASS, PINK_STAINED_GLASS_PANE,
                 PURPLE_STAINED_GLASS, PURPLE_STAINED_GLASS_PANE,
                 RED_STAINED_GLASS, RED_STAINED_GLASS_PANE,
                 YELLOW_STAINED_GLASS, YELLOW_STAINED_GLASS_PANE,
                 TINTED_GLASS -> true;
            default -> false;
        };
    }

    public static boolean isShulkerBox(final @NotNull Material material) {
        return switch (material) {
            case SHULKER_BOX, BLACK_SHULKER_BOX, BLUE_SHULKER_BOX,
                 BROWN_SHULKER_BOX, CYAN_SHULKER_BOX, GRAY_SHULKER_BOX,
                 GREEN_SHULKER_BOX, LIGHT_BLUE_SHULKER_BOX, LIGHT_GRAY_SHULKER_BOX,
                 LIME_SHULKER_BOX, MAGENTA_SHULKER_BOX, RED_SHULKER_BOX,
                 ORANGE_SHULKER_BOX, PINK_SHULKER_BOX, YELLOW_SHULKER_BOX,
                 PURPLE_SHULKER_BOX, WHITE_SHULKER_BOX -> true;
            default -> false;
        };
    }

    public static boolean isChest(final @NotNull Material material) {
        return switch (material) {
            case CHEST, ENDER_CHEST, TRAPPED_CHEST -> true;
            default -> false;
        };
    }

    public static boolean hasAction(final @NotNull Material material) {
        if (!material.isBlock()) return false;
        final String name = material.toString();
//        Shulker boxes
        if (isShulkerBox(material)) return true;

//        Chest, Ender Chest, Trapped Chest
        if (isChest(material)) return true;

//        Crafting Table, Smithing Table, Cartography Table, Enchanting Table
//        Fletching Table too, but who cares about Fletching Table
        if (name.contains("TABLE")) return true;

//        Signs
        if (name.contains("SIGN")) return true;

//        Doors and Trapdoors
        if (name.contains("DOOR")) return true;

//        Furnace, Blast Furnace
        if (name.contains("FURNACE")) return true;

//        Anvils
        if (name.contains("ANVIL")) return true;

//        Fence gates
        if (name.contains("FENCE_GATE")) return true;

//        Buttons
        if (name.contains("BUTTON")) return true;

//        Command Blocks
        if (name.contains("COMMAND_BLOCK")) return true;
        return OTHER_ACTION_BLOCKS.contains(material);
    }
}
