package cz.jeme.programu.gungaming.item.ammo;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

public final class NineMm extends Ammo {
    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public @NotNull String getName() {
        return "9mm";
    }

    @Override
    public @NotNull String getInfo() {
        return "Ammo for basic weapons";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public int getMinStackLoot() {
        return 7;
    }

    @Override
    public int getMaxStackLoot() {
        return 18;
    }
}
