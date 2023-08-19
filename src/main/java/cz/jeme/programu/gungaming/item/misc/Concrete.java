package cz.jeme.programu.gungaming.item.misc;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class Concrete extends Misc {
    @Override
    protected void setup() {
        name = "Concrete";
        info = "A basic building block";
        customModelData = 0;
        rarity = Rarity.COMMON;
    }

    @Override
    protected @NotNull Material getMaterial() {
        return Material.GRAY_CONCRETE;
    }

    @Override
    public int getMinLoot() {
        return 10;
    }

    @Override
    public int getMaxLoot() {
        return 16;
    }
}
