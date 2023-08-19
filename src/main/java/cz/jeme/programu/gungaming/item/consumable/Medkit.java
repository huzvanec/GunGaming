package cz.jeme.programu.gungaming.item.consumable;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class Medkit extends Heal {
    @Override
    protected void setup() {
        name = "Medkit";
        info = "instantly fully heals you";
        customModelData = 1;
        rarity = Rarity.EPIC;
        heal = 999999999d;
    }

    @Override
    protected @NotNull Material getMaterial() {
        return Material.MILK_BUCKET;
    }

    @Override
    public int getMinLoot() {
        return 1;
    }

    @Override
    public int getMaxLoot() {
        return 1;
    }
}
