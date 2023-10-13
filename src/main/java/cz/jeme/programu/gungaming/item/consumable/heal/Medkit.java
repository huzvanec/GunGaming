package cz.jeme.programu.gungaming.item.consumable.heal;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public final class Medkit extends Heal {

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public @NotNull String getName() {
        return "Medkit";
    }

    @Override
    public @NotNull String getInfo() {
        return "Instantly gets you to full health";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public @NotNull Material getMaterial() {
        return Material.MILK_BUCKET;
    }

    @Override
    public int getMinStackLoot() {
        return 1;
    }

    @Override
    public int getMaxStackLoot() {
        return 1;
    }

    @Override
    public double getHealAmount() {
        return Float.MAX_VALUE;
    }
}