package cz.jeme.programu.gungaming.item.ammo;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class NineMM extends Ammo {

    @Override
    protected void setup() {
        material = Material.BROWN_DYE;
        name = "9mm";
        info = "Ammo for basic weapons";
        rarity = Rarity.UNCOMMON;
        minLoot = 5;
        maxLoot = 16;
    }

}
