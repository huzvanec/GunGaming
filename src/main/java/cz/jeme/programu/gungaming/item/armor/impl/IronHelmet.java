package cz.jeme.programu.gungaming.item.armor.impl;

import cz.jeme.programu.gungaming.item.armor.Helmet;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class IronHelmet extends Helmet {
    @Override
    protected double provideArmor() {
        return 2;
    }

    @Override
    protected double provideToughness() {
        return 0;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "basic helmet";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.IRON_HELMET;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "iron_helmet";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Iron Helmet");
    }
}
