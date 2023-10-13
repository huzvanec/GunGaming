package cz.jeme.programu.gungaming.item.ammo;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

public final class TwelveGauge extends Ammo {
    @Override
    public int getCustomModelData() {
        return 4;
    }

    @Override
    public @NotNull String getName() {
        return "12 gauge";
    }

    @Override
    public @NotNull String getInfo() {
        return "Ammo for shotguns";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    public int getMinStackLoot() {
        return 3;
    }

    @Override
    public int getMaxStackLoot() {
        return 10;
    }
}
