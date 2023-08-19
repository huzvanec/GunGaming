package cz.jeme.programu.gungaming.item.attachment.magazine;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

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
    protected @NotNull String[] getBuffs() {
        return new String[]{"+10% ammo"};
    }

    @Override
    protected @NotNull String[] getNerfs() {
        return new String[]{"-10% reload speed"};
    }
}
