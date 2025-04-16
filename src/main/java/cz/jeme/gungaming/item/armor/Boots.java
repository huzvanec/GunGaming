package cz.jeme.gungaming.item.armor;

import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class Boots extends Armor {
    protected Boots() {
        addTags("boots");
    }

    @Override
    protected final String provideType() {
        return "boots";
    }

    @Override
    protected final EquipmentSlot provideSlot() {
        return EquipmentSlot.FEET;
    }
}
