package cz.jeme.programu.gungaming.item.ammo;

import cz.jeme.programu.gungaming.loot.Rarity;

public class NineMm extends Ammo {

    @Override
    protected void setup() {
        customModelData = 1;
        name = "9mm";
        info = "Ammo for basic weapons";
        rarity = Rarity.COMMON;
    }

    @Override
    public int getMinLoot() {
        return 7;
    }

    @Override
    public int getMaxLoot() {
        return 18;
    }

}
