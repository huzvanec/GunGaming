package cz.jeme.programu.gungaming.item.melee.impl;

import cz.jeme.programu.gungaming.item.melee.Melee;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class IronSword extends Melee {
    @Override
    protected @NotNull Component provideName() {
        return Component.text("Iron Sword");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "A basic melee weapon";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.COMMON;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "iron_sword";
    }

    @Override
    protected double provideDamage() {
        return 8;
    }
}
