package cz.jeme.programu.gungaming.item.armor.impl;

import cz.jeme.programu.gungaming.item.armor.Helmet;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class DiamondHelmet extends Helmet {
    @Override
    protected double provideArmor() {
        return 2;
    }

    @Override
    protected double provideToughness() {
        return 1;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "good helmet";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.DIAMOND_HELMET;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "diamond_helmet";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Diamond Helmet");
    }
}
