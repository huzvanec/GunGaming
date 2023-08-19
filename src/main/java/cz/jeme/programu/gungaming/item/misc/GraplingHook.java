package cz.jeme.programu.gungaming.item.misc;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class GraplingHook extends Misc {
    @Override
    protected void setup() {
        name = "Grapling Hook";
        info = "You won't catch much fish with this";
        rarity = Rarity.EPIC;
        customModelData = 1;
    }

    @Override
    protected @NotNull Material getMaterial() {
        return Material.FISHING_ROD;
    }

    @Override
    public int getMinLoot() {
        return 1;
    }

    @Override
    public int getMaxLoot() {
        return 1;
    }
}
