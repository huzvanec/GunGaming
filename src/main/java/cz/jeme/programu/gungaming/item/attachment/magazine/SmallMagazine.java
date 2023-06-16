package cz.jeme.programu.gungaming.item.attachment.magazine;

import cz.jeme.programu.gungaming.item.attachment.ModifiersInfo;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class SmallMagazine extends Magazine {
    @Override
    protected void setup() {
        material = Material.STONE_PICKAXE;
        name = "Small Magazine";
        info = "A small extended storage for ammo";
        rarity = Rarity.RARE;
        minLoot = 1;
        maxLoot = 1;
        magazinePercentage = 110f;
        modifiersInfo = new ModifiersInfo();
        modifiersInfo.addBuff("+10% ammo");
        modifiersInfo.addNerf("-10% reload speed");
    }
}
