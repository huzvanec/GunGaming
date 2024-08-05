package cz.jeme.programu.gungaming.item.armor.impl;

import cz.jeme.programu.gungaming.item.armor.Leggings;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class TitaniumLeggings extends Leggings {
    @Override
    protected double provideArmor() {
        return 8;
    }

    @Override
    protected double provideToughness() {
        return 3;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.NETHERITE_LEGGINGS;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "titanium_leggings";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Titanium Leggings");
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
    }
}
