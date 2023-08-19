package cz.jeme.programu.gungaming.item.attachment.magazine;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

public class BigMagazine extends Magazine {
    @Override
    protected void setup() {
        customModelData = 2;
        name = "Big Magazine";
        info = "A big extended storage for ammo";
        rarity = Rarity.EPIC;
        magazinePercentage = 130f;
    }


    @Override
    protected @NotNull String[] getBuffs() {
        return new String[]{"+30% ammo"};
    }

    @Override
    protected @NotNull String[] getNerfs() {
        return new String[]{"-30% reload speed"};
    }
}
