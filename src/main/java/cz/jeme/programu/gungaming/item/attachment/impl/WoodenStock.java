package cz.jeme.programu.gungaming.item.attachment.impl;

import cz.jeme.programu.gungaming.item.attachment.Stock;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WoodenStock extends Stock {
    @Override
    protected double provideRecoilMultiplier() {
        return 0.8;
    }

    @Override
    protected @NotNull List<String> provideBuffs() {
        return List.of(
                "-20% recoil"
        );
    }

    @Override
    protected @NotNull List<String> provideDebuffs() {
        return List.of();
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "wooden_stock";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Wooden Stock");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Good weapon stability";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 11;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }
}
