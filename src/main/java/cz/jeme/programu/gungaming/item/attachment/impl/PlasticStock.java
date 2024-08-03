package cz.jeme.programu.gungaming.item.attachment.impl;

import cz.jeme.programu.gungaming.item.attachment.Stock;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlasticStock extends Stock {
    @Override
    protected double provideRecoilMultiplier() {
        return 0.5;
    }

    @Override
    protected @NotNull List<String> provideBuffs() {
        return List.of(
                "-50% recoil"
        );
    }

    @Override
    protected @NotNull List<String> provideDebuffs() {
        return List.of();
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "plastic_stock";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Plastic Stock");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Great weapon stability";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 12;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }
}
