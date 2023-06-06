package cz.jeme.programu.gungaming.item.misc;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class Concrete extends Misc {
    @Override
    protected void setup() {
        material = Material.GRAY_CONCRETE;
        name = "Concrete";
        info = "A default building block";
        rarity = Rarity.COMMON;
        minLoot = 10;
        maxLoot = 16;
    }
}
