package cz.jeme.programu.gungaming.item.armor;

import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

public abstract class Boots extends Armor {
    protected Boots() {
        addTags("boots");
        switch (material) {
            case CHAINMAIL_BOOTS, DIAMOND_BOOTS,
                 GOLDEN_BOOTS, IRON_BOOTS,
                 LEATHER_BOOTS, NETHERITE_BOOTS -> {
            }
            default -> throw new IllegalArgumentException("Material must be boots!");
        }
    }

    @Override
    protected final @NotNull String provideType() {
        return "boots";
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    protected final @NotNull EquipmentSlotGroup provideSlot() {
        return EquipmentSlotGroup.FEET;
    }
}
