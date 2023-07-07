package cz.jeme.programu.gungaming.item.attachment.stock;

import cz.jeme.programu.gungaming.loot.Rarity;

public class WoodenStock extends Stock {
    @Override
    protected void setup() {
        customModelData = 1;
        name = "Wooden Stock";
        info = "Poor weapon stability";
        rarity = Rarity.RARE;
        recoilPercentage = 80f;
        inaccuracyPercentage = 70f;
    }

    @Override
    protected String[] getBuffs() {
        return new String[]{"-20% recoil", "+30% accuracy"};
    }

    @Override
    protected String[] getNerfs() {
        return new String[0];
    }
}
