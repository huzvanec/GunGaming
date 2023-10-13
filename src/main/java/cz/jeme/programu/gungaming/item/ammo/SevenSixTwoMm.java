package cz.jeme.programu.gungaming.item.ammo;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

public final class SevenSixTwoMm extends Ammo {
    @Override
    public int getCustomModelData() {
        return 2;
    }

    @Override
    public @NotNull String getName() {
        return "7.62mm";
    }

    @Override
    public @NotNull String getInfo() {
        return "Ammo for better weapons";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public int getMinStackLoot() {
        return 5;
    }

    @Override
    public int getMaxStackLoot() {
        return 12;
    }
}
