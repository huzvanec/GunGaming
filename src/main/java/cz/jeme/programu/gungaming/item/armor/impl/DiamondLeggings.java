package cz.jeme.programu.gungaming.item.armor.impl;

import cz.jeme.programu.gungaming.item.armor.Leggings;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class DiamondLeggings extends Leggings {
    @Override
    protected double provideArmor() {
        return 5;
    }

    @Override
    protected double provideToughness() {
        return 1;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "good leggings";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.DIAMOND_LEGGINGS;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "diamond_leggings";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Diamond Leggings");
    }
}
