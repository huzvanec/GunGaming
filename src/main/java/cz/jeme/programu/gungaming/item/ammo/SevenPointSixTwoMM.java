package cz.jeme.programu.gungaming.item.ammo;

import cz.jeme.programu.gungaming.loot.Rarity;

public class SevenPointSixTwoMM extends Ammo {

    @Override
    protected void setup() {
        customModelData = 2;
        name = "7.62mm";
        info = "Ammo for better weapons";
        rarity = Rarity.UNCOMMON;
    }

    @Override
    public int getMinLoot() {
        return 5;
    }

    @Override
    public int getMaxLoot() {
        return 12;
    }

}
