package cz.jeme.programu.gungaming.item.ammo;

import cz.jeme.programu.gungaming.loot.Rarity;

public class TwelveGauge extends Ammo {

    @Override
    protected void setup() {
        customModelData = 4;
        name = "12 gauge";
        info = "Ammo for shotguns";
        rarity = Rarity.UNCOMMON;
    }

    @Override
    public int getMinLoot() {
        return 3;
    }

    @Override
    public int getMaxLoot() {
        return 10;
    }

}
