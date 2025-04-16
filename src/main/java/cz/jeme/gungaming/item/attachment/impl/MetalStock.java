package cz.jeme.gungaming.item.attachment.impl;

import cz.jeme.gungaming.item.attachment.Stock;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class MetalStock extends Stock {
    @Override
    protected double provideRecoilMultiplier() {
        return 0.2;
    }

    @Override
    protected List<String> provideBuffs() {
        return List.of(
                "-80% recoil"
        );
    }

    @Override
    protected List<String> provideDebuffs() {
        return List.of();
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "metal_stock";
    }

    @Override
    protected Component provideName() {
        return Component.text("Metal Stock");
    }

    @Override
    protected String provideDescription() {
        return "Awesome weapon stability";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }
}
