package cz.jeme.programu.gungaming.item.armor.impl;

import cz.jeme.programu.gungaming.item.armor.Boots;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class TitaniumBoots extends Boots {
    @Override
    protected double provideArmor() {
        return 5;
    }

    @Override
    protected double provideToughness() {
        return 3;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "boots forged from raw titanium";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.NETHERITE_BOOTS;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "titanium_boots";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Titanium Boots");
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
    }
}
