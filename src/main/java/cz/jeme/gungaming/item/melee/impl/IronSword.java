package cz.jeme.gungaming.item.melee.impl;

import cz.jeme.gungaming.item.melee.Sword;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class IronSword extends Sword {
    @Override
    protected Component provideName() {
        return Component.text("Iron Sword");
    }

    @Override
    protected String provideDescription() {
        return "basic sword";
    }

    @Override
    protected Material provideMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.COMMON;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "iron_sword";
    }

    @Override
    protected double provideDamageBonus() {
        return 7;
    }
}
