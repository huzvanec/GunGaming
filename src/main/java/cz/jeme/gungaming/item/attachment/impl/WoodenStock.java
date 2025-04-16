package cz.jeme.gungaming.item.attachment.impl;

import cz.jeme.gungaming.item.attachment.Stock;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class WoodenStock extends Stock {
    @Override
    protected double provideRecoilMultiplier() {
        return 0.8;
    }

    @Override
    protected List<String> provideBuffs() {
        return List.of(
                "-20% recoil"
        );
    }

    @Override
    protected List<String> provideDebuffs() {
        return List.of();
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "wooden_stock";
    }

    @Override
    protected Component provideName() {
        return Component.text("Wooden Stock");
    }

    @Override
    protected String provideDescription() {
        return "Good weapon stability";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.RARE;
    }
}
