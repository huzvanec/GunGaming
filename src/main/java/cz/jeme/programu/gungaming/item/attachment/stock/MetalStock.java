package cz.jeme.programu.gungaming.item.attachment.stock;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

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
    protected @NotNull String[] getBuffs() {
        return new String[]{"-80% recoil", "+90% accuracy"};
    }

    @Override
    protected @NotNull String[] getNerfs() {
        return new String[0];
    }
}
