package cz.jeme.programu.gungaming.item.armor;

import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

public abstract class Leggings extends Armor {
    protected Leggings() {
        addTags("leggings");
        switch (material) {
            case CHAINMAIL_LEGGINGS, DIAMOND_LEGGINGS,
                 GOLDEN_LEGGINGS, IRON_LEGGINGS,
                 LEATHER_LEGGINGS, NETHERITE_LEGGINGS -> {
            }
            default -> throw new IllegalArgumentException("Material must be leggings!");
        }
    }

    @Override
    protected final @NotNull String provideType() {
        return "leggings";
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    protected final @NotNull EquipmentSlotGroup provideSlot() {
        return EquipmentSlotGroup.LEGS;
    }
}
