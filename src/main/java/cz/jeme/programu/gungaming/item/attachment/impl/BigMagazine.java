package cz.jeme.programu.gungaming.item.attachment.impl;

import cz.jeme.programu.gungaming.item.attachment.Magazine;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BigMagazine extends Magazine {
    @Override
    protected double provideMaxAmmoMultiplier() {
        return 1.3;
    }

    @Override
    protected @NotNull List<String> provideBuffs() {
        return List.of(
                "+30% ammo"
        );
    }

    @Override
    protected @NotNull List<String> provideDebuffs() {
        return List.of(
                "-30% reload speed"
        );
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "big_magazine";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Big Magazine");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Big extended storage for ammo";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 9;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }
}
