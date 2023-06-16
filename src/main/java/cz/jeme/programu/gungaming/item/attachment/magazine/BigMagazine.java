package cz.jeme.programu.gungaming.item.attachment.magazine;

import cz.jeme.programu.gungaming.item.attachment.ModifiersInfo;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class BigMagazine extends Magazine {
    @Override
    protected void setup() {
        material = Material.STONE_AXE;
        name = "Big Magazine";
        info = "A big extended storage for ammo";
        rarity = Rarity.EPIC;
        minLoot = 1;
        maxLoot = 1;
        magazinePercentage = 130f;
        modifiersInfo = new ModifiersInfo();
        modifiersInfo.addBuff("+30% ammo");
        modifiersInfo.addNerf("-30% reload speed");
    }
}
