package cz.jeme.gungaming.item.attachment.impl;

import cz.jeme.gungaming.item.attachment.Scope;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class HighScope extends Scope {
    @Override
    protected double provideZoom() {
        return 10;
    }

    @Override
    protected List<String> provideBuffs() {
        return List.of(
                "+10× scope"
        );
    }

    @Override
    protected List<String> provideDebuffs() {
        return List.of();
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "high_scope";
    }

    @Override
    protected Component provideName() {
        return Component.text("High Scope");
    }

    @Override
    protected String provideDescription() {
        return "Extreme scope for long-range";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }
}
