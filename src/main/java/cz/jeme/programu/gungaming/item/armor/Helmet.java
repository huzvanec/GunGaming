package cz.jeme.programu.gungaming.item.armor;

import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

public abstract class Helmet extends Armor {
    protected Helmet() {
        addTags("helmet");
        switch (material) {
            case CHAINMAIL_HELMET, DIAMOND_HELMET,
                 GOLDEN_HELMET, IRON_HELMET,
                 LEATHER_HELMET, NETHERITE_HELMET,
                 TURTLE_HELMET -> {
            }
            default -> throw new IllegalArgumentException("Material must be a helmet!");
        }
    }

    @Override
    protected final @NotNull String provideType() {
        return "helmet";
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    protected final @NotNull EquipmentSlotGroup provideSlot() {
        return EquipmentSlotGroup.HEAD;
    }
}
