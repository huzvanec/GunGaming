package cz.jeme.programu.gungaming.util;

import org.bukkit.Material;

import java.util.Arrays;

public class Materials {

    private static final Material[] OTHERS = {
            Material.BARREL, Material.SMOKER, Material.DISPENSER,
            Material.DROPPER, Material.HOPPER, Material.GRINDSTONE,
            Material.LOOM, Material.STONECUTTER, Material.BREWING_STAND,
            Material.BEACON, Material.REDSTONE, Material.LEVER,
            Material.REPEATER, Material.COMPARATOR, Material.REDSTONE_ORE,
            Material.NOTE_BLOCK, Material.DAYLIGHT_DETECTOR, Material.BELL
    };

    private Materials() {
    }

    public static boolean isGlass(Material material) {
        return material.toString().contains("GLASS");
    }

    public static boolean hasRightClick(Material material) {
        String name = material.toString();
//        Shulker boxes
        if (name.contains("SHULKER_BOX")) return true;

//        Chest, Ender Chest, Trapped Chest
        if (name.contains("CHEST")) return true;

//        Crafting Table, Smithing Table, Cartography Table, Enchanting Table
//        Fletching Table too, but f*** Fletching Table
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

        return Arrays.asList(OTHERS).contains(material);
    }
}
