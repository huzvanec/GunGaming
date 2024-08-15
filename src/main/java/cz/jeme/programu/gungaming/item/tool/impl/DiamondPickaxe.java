package cz.jeme.programu.gungaming.item.tool.impl;

import cz.jeme.programu.gungaming.item.tool.Tool;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class DiamondPickaxe extends Tool {
    @Override
    protected @NotNull String provideDescription() {
        return "great for breaking concrete in the way";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.DIAMOND_PICKAXE;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "diamond_pickaxe";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Diamond Pickaxe");
    }
}
