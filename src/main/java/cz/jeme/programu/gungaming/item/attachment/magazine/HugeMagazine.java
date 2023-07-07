package cz.jeme.programu.gungaming.item.attachment.magazine;

import cz.jeme.programu.gungaming.loot.Rarity;

public class HugeMagazine extends Magazine {
    @Override
    protected void setup() {
        customModelData = 3;
        name = "Huge Magazine";
        info = "A huge extended storage for ammo";
        rarity = Rarity.LEGENDARY;
        magazinePercentage = 150f;
    }

    @Override
    protected String[] getBuffs() {
        return new String[]{"+50% ammo"};
    }

    @Override
    protected String[] getNerfs() {
        return new String[]{"-50% reload speed"};
    }
}
