package cz.jeme.programu.gungaming.item.attachment.magazine;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class BigMagazine extends Magazine {
    @Override
    protected void setup() {
        material = Material.STONE_AXE;
        name = "Big Magazine";
        info = "+30% ammo";
        rarity = Rarity.EPIC;
        minLoot = 1;
        maxLoot = 1;
        magazinePercentage = 130f;
    }
}
