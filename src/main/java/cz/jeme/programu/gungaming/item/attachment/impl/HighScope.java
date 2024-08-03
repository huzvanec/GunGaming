package cz.jeme.programu.gungaming.item.attachment.impl;

import cz.jeme.programu.gungaming.item.attachment.Scope;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HighScope extends Scope {
    @Override
    protected double provideZoom() {
        return 10;
    }

    @Override
    protected @NotNull List<String> provideBuffs() {
        return List.of(
                "+10× scope"
        );
    }

    @Override
    protected @NotNull List<String> provideDebuffs() {
        return List.of();
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "high_scope";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("High Scope");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Extreme scope for long-range";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 7;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }
}
