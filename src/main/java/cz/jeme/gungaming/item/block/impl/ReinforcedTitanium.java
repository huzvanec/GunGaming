package cz.jeme.gungaming.item.block.impl;

import cz.jeme.gungaming.item.block.CustomBlock;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ReinforcedTitanium extends CustomBlock {
    @Override
    protected Component provideName() {
        return Component.text("Reinforced Titanium");
    }

    @Override
    protected String provideDescription() {
        return "Hard material, resists explosions";
    }

    @Override
    protected Material provideMaterial() {
        return Material.NETHERITE_BLOCK;
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected int provideMinAmount() {
        return 5;
    }

    @Override
    protected int provideMaxAmount() {
        return 15;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "reinforced_titanium";
    }
}
