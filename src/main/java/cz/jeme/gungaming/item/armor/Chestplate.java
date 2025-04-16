package cz.jeme.gungaming.item.armor;

import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class Chestplate extends Armor {
    protected Chestplate() {
        addTags("chestplate");
    }

    @Override
    protected final String provideType() {
        return "chestplate";
    }

    @Override
    protected final EquipmentSlot provideSlot() {
        return EquipmentSlot.CHEST;
    }
}
