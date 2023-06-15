package cz.jeme.programu.gungaming.item.attachment.magazine;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class SmallMagazine extends Magazine {
    @Override
    protected void setup() {
        material = Material.STONE_PICKAXE;
        name = "Small Magazine";
        info = "+10% ammo";
        rarity = Rarity.RARE;
        minLoot = 1;
        maxLoot = 1;
        magazinePercentage = 110f;
    }
}
