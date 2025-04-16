package cz.jeme.gungaming.item.melee.impl;

import cz.jeme.gungaming.item.melee.Sword;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DiamondSword extends Sword {
    @Override
    protected Component provideName() {
        return Component.text("Diamond Sword");
    }

    @Override
    protected String provideDescription() {
        return "good sword";
    }

    @Override
    protected Material provideMaterial() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "diamond_sword";
    }

    @Override
    protected double provideDamageBonus() {
        return 9;
    }
}
