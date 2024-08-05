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
        return 3;
    }

    @Override
    protected double provideToughness() {
        return 2;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.DIAMOND_HELMET;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "titanium_helmet";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Titanium Helmet");
    }
}
