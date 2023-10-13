package cz.jeme.programu.gungaming.item.consumable.heal;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public final class Bandage extends Heal {
    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public @NotNull String getName() {
        return "Bandage";
    }

    @Override
    public @NotNull String getInfo() {
        return "Instantly heals 2.5 hearts";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public @NotNull Material getMaterial() {
        return Material.GOLDEN_APPLE;
    }

    @Override
    public int getMinStackLoot() {
        return 1;
    }

    @Override
    public int getMaxStackLoot() {
        return 5;
    }

    @Override
    public double getHealAmount() {
        return 2.5D;
    }
}