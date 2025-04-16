package cz.jeme.gungaming.item.armor;

import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class Leggings extends Armor {
    protected Leggings() {
        addTags("leggings");
    }

    @Override
    protected final String provideType() {
        return "leggings";
    }

    @Override
    protected final EquipmentSlot provideSlot() {
        return EquipmentSlot.LEGS;
    }
}
