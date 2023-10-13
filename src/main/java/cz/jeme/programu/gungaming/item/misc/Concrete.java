package cz.jeme.programu.gungaming.item.misc;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public final class Concrete extends Misc {
    @Override
    public int getCustomModelData() {
        return 0;
    }

    @Override
    public @NotNull String getName() {
        return "Concrete";
    }

    @Override
    public @NotNull String getInfo() {
        return "Basic building block";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public @NotNull Material getMaterial() {
        return Material.GRAY_CONCRETE;
    }

    @Override
    public int getMinStackLoot() {
        return 10;
    }

    @Override
    public int getMaxStackLoot() {
        return 20;
    }
}