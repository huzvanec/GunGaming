package cz.jeme.programu.gungaming.item.consumable;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class Bandage extends Heal {
    @Override
    protected void setup() {
        name = "Bandage";
        info = "instantly heals 2 hearts";
        customModelData = 1;
        rarity = Rarity.RARE;
        heal = 4d;
    }

    @Override
    protected @NotNull Material getMaterial() {
        return Material.GOLDEN_APPLE;
    }

    @Override
    public int getMinLoot() {
        return 1;
    }

    @Override
    public int getMaxLoot() {
        return 5;
    }
}
