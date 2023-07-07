package cz.jeme.programu.gungaming.item.attachment.stock;

import cz.jeme.programu.gungaming.loot.Rarity;

public class PlasticStock extends Stock {
    @Override
    protected void setup() {
        customModelData = 2;
        name = "Plastic Stock";
        info = "Great weapon stability";
        rarity = Rarity.EPIC;
        recoilPercentage = 50f;
        inaccuracyPercentage = 40f;
    }

    @Override
    protected String[] getBuffs() {
        return new String[]{"-50% recoil", "+60% accuracy"};
    }

    @Override
    protected String[] getNerfs() {
        return new String[0];
    }
}
