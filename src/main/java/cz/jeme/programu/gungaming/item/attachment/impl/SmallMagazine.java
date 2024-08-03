package cz.jeme.programu.gungaming.item.attachment.impl;

import cz.jeme.programu.gungaming.item.attachment.Magazine;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SmallMagazine extends Magazine {
    @Override
    protected double provideMaxAmmoMultiplier() {
        return 1.1;
    }

    @Override
    protected @NotNull List<String> provideBuffs() {
        return List.of(
                "+10% ammo"
        );
    }

    @Override
    protected @NotNull List<String> provideDebuffs() {
        return List.of(
                "-10% reload speed"
        );
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "small_magazine";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Small Magazine");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Small extended storage for ammo";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 8;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }
}
