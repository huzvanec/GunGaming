package cz.jeme.programu.gungaming.item.ammo;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

public final class ThreeZeroEightSubsonicWinchester extends Ammo {
    @Override
    public int getCustomModelData() {
        return 5;
    }

    @Override
    public @NotNull String getName() {
        return ".308 Subsonic Winchester";
    }

    @Override
    public @NotNull String getInfo() {
        return "Ammo for the best sniper rifles";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public int getMinStackLoot() {
        return 3;
    }

    @Override
    public int getMaxStackLoot() {
        return 12;
    }
}
