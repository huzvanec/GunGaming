package cz.jeme.programu.gungaming.item.attachment.magazine;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class HugeMagazine extends Magazine {
    @Override
    protected void setup() {
        material = Material.WOODEN_AXE;
        name = "Huge Magazine";
        info = "+50% ammo";
        rarity = Rarity.LEGENDARY;
        minLoot = 1;
        maxLoot = 1;
        magazinePercentage = 150f;
    }
}
