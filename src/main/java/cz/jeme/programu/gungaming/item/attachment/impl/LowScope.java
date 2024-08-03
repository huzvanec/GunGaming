package cz.jeme.programu.gungaming.item.attachment.impl;

import cz.jeme.programu.gungaming.item.attachment.Scope;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LowScope extends Scope {
    @Override
    protected double provideZoom() {
        return 2;
    }

    @Override
    protected @NotNull List<String> provideBuffs() {
        return List.of(
                "+2× scope"
        );
    }

    @Override
    protected @NotNull List<String> provideDebuffs() {
        return List.of();
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "low_scope";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Low Scope");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Basic scope for close-range";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 5;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }
}
