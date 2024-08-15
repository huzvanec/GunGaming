package cz.jeme.programu.gungaming.item.armor.impl;

import cz.jeme.programu.gungaming.item.armor.Chestplate;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class TitaniumChestplate extends Chestplate {
    @Override
    protected double provideArmor() {
        return 8;
    }

    @Override
    protected double provideToughness() {
        return 2;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "chestplate forged from raw titanium";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.NETHERITE_CHESTPLATE;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "titanium_chestplate";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Titanium Chestplate");
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
    }
}
