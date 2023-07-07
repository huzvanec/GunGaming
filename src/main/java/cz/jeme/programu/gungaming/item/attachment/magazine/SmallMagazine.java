package cz.jeme.programu.gungaming.item.attachment.magazine;

import cz.jeme.programu.gungaming.loot.Rarity;

public class SmallMagazine extends Magazine {
    @Override
    protected void setup() {
        customModelData = 1;
        name = "Small Magazine";
        info = "A small extended storage for ammo";
        rarity = Rarity.RARE;
        magazinePercentage = 110f;
    }

    @Override
    protected String[] getBuffs() {
        return new String[]{"+10% ammo"};
    }

    @Override
    protected String[] getNerfs() {
        return new String[]{"-10% reload speed"};
    }
}
