package cz.jeme.programu.gungaming.item.tool.impl;

import cz.jeme.programu.gungaming.item.tool.Tool;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class DiamondShovel extends Tool {
    @Override
    protected @NotNull String provideDescription() {
        return "great for breaking dirt in the way";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.DIAMOND_SHOVEL;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "diamond_shovel";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Diamond Shovel");
    }
}
