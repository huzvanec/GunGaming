package cz.jeme.programu.gungaming.item.attachment.stock;

import cz.jeme.programu.gungaming.loot.Rarity;

public class MetalStock extends Stock {
    @Override
    protected void setup() {
        customModelData = 3;
        name = "Metal Stock";
        info = "Awesome weapon handling";
        rarity = Rarity.LEGENDARY;
        recoilPercentage = 20f;
        inaccuracyPercentage = 10f;
    }

    @Override
    protected String[] getBuffs() {
        return new String[]{"-80% recoil", "+90% accuracy"};
    }

    @Override
    protected String[] getNerfs() {
        return new String[0];
    }
}
