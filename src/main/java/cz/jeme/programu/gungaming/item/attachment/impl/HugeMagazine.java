package cz.jeme.programu.gungaming.item.attachment.impl;

import cz.jeme.programu.gungaming.item.attachment.Magazine;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HugeMagazine extends Magazine {

    @Override
    protected double provideMaxAmmoMultiplier() {
        return 1.5;
    }

    @Override
    protected @NotNull List<String> provideBuffs() {
        return List.of(
                "+50% ammo"
        );
    }

    @Override
    protected @NotNull List<String> provideDebuffs() {
        return List.of(
                "-50% reload speed"
        );
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "huge_magazine";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Huge Magazine");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Huge extended storage for ammo";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 10;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }
}
