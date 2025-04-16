package cz.jeme.gungaming.item.attachment.impl;

import cz.jeme.gungaming.item.attachment.Stock;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class PlasticStock extends Stock {
    @Override
    protected double provideRecoilMultiplier() {
        return 0.5;
    }

    @Override
    protected List<String> provideBuffs() {
        return List.of(
                "-50% recoil"
        );
    }

    @Override
    protected List<String> provideDebuffs() {
        return List.of();
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "plastic_stock";
    }

    @Override
    protected Component provideName() {
        return Component.text("Plastic Stock");
    }

    @Override
    protected String provideDescription() {
        return "Great weapon stability";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.EPIC;
    }
}
