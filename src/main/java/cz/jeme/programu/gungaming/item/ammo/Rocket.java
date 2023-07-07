package cz.jeme.programu.gungaming.item.ammo;

import cz.jeme.programu.gungaming.loot.Rarity;

public class Rocket extends Ammo {

    @Override
    protected void setup() {
        customModelData = 3;
        name = "Rocket";
        info = "Ammo for the Rocket Launcher";
        rarity = Rarity.EPIC;
    }

    @Override
    public int getMinLoot() {
        return 1;
    }

    @Override
    public int getMaxLoot() {
        return 4;
    }

}
