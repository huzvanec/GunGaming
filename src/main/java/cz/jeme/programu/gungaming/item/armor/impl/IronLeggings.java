package cz.jeme.programu.gungaming.item.armor.impl;

import cz.jeme.programu.gungaming.item.armor.Leggings;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class IronLeggings extends Leggings {
    @Override
    protected double provideArmor() {
        return 5;
    }

    @Override
    protected double provideToughness() {
        return 0;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.IRON_LEGGINGS;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "iron_leggings";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Iron Leggings");
    }
}
