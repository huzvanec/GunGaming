package cz.jeme.programu.gungaming.item.armor;

import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

public abstract class Chestplate extends Armor {
    protected Chestplate() {
        addTags("chestplate");
        switch (material) {
            case CHAINMAIL_CHESTPLATE, DIAMOND_CHESTPLATE,
                 GOLDEN_CHESTPLATE, IRON_CHESTPLATE,
                 LEATHER_CHESTPLATE, NETHERITE_CHESTPLATE -> {
            }
            default -> throw new IllegalArgumentException("Material must be a chestplate!");
        }
    }

    @Override
    protected final @NotNull String provideType() {
        return "chestplate";
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    protected final @NotNull EquipmentSlotGroup provideSlot() {
        return EquipmentSlotGroup.CHEST;
    }
}
