package cz.jeme.programu.gungaming.item.armor.impl;

import cz.jeme.programu.gungaming.item.armor.Helmet;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class TitaniumHelmet extends Helmet {
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
        return "helmet forged from raw titanium";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.NETHERITE_HELMET;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "titanium_helmet";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Titanium Helmet");
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
    }
}
