package cz.jeme.gungaming.item.armor;

import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class Helmet extends Armor {
    protected Helmet() {
        addTags("helmet");
    }

    @Override
    protected final String provideType() {
        return "helmet";
    }

    @Override
    protected final EquipmentSlot provideSlot() {
        return EquipmentSlot.HEAD;
    }
}
