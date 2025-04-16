package cz.jeme.gungaming.item.attachment.impl;

import cz.jeme.gungaming.item.attachment.Scope;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class MediumScope extends Scope {
    @Override
    protected double provideZoom() {
        return 5;
    }

    @Override
    protected List<String> provideBuffs() {
        return List.of(
                "+5× scope"
        );
    }

    @Override
    protected List<String> provideDebuffs() {
        return List.of();
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "medium_scope";
    }

    @Override
    protected Component provideName() {
        return Component.text("Medium Scope");
    }

    @Override
    protected String provideDescription() {
        return "Precise scope for mid-range";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.EPIC;
    }
}
