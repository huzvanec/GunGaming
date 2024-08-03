package cz.jeme.programu.gungaming.item.attachment.impl;

import cz.jeme.programu.gungaming.item.attachment.Scope;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MediumScope extends Scope {
    @Override
    protected double provideZoom() {
        return 5;
    }

    @Override
    protected @NotNull List<String> provideBuffs() {
        return List.of(
                "+5× scope"
        );
    }

    @Override
    protected @NotNull List<String> provideDebuffs() {
        return List.of();
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "medium_scope";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Medium Scope");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Precise scope for mid-range";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 6;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }
}
