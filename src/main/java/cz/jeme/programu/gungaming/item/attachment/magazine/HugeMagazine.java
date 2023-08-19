package cz.jeme.programu.gungaming.item.attachment.magazine;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

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
    protected @NotNull String[] getBuffs() {
        return new String[]{"+50% ammo"};
    }

    @Override
    protected @NotNull String[] getNerfs() {
        return new String[]{"-50% reload speed"};
    }
}
