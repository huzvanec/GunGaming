package cz.jeme.programu.gungaming.item.melee.impl;

import cz.jeme.programu.gungaming.item.melee.Melee;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class DiamondSword extends Melee {
    @Override
    protected @NotNull Component provideName() {
        return Component.text("Diamond Sword");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "good sword";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "diamond_sword";
    }

    @Override
    protected double provideDamage() {
        return 12;
    }
}
