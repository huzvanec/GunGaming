package cz.jeme.programu.gungaming.item.ammo;

import cz.jeme.programu.gungaming.loot.Rarity;

public class ThreeZeroEightSubsonicWinchester extends Ammo {
    @Override
    protected void setup() {
        customModelData = 5;
        name = ".308 Subsonic Winchester";
        info = "Ammo for the best sniper rifles";
        rarity = Rarity.EPIC;
    }

    @Override
    public int getMinLoot() {
        return 3;
    }

    @Override
    public int getMaxLoot() {
        return 12;
    }
}
