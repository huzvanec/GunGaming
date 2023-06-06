package cz.jeme.programu.gungaming.item.ammo;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class SevenPointSixTwoMM extends Ammo {

    @Override
    protected void setup() {
        material = Material.BLACK_DYE;
        name = "7.62mm";
        info = "Common ammo";
        rarity = Rarity.UNCOMMON;
        minLoot = 5;
        maxLoot = 12;
    }

}
