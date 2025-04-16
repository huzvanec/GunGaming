package cz.jeme.gungaming.item.consumable.impl;

import cz.jeme.gungaming.item.consumable.Adrenaline;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;

import java.util.Set;

@NullMarked
public class Soda extends Adrenaline {
    @Override
    protected Set<PotionEffect> provideEffects() {
        return Set.of(
                new PotionEffect(
                        PotionEffectType.REGENERATION,
                        600,
                        0,
                        false,
                        true,
                        false
                ),
                new PotionEffect(
                        PotionEffectType.SPEED,
                        600,
                        0,
                        false,
                        true,
                        false
                )
        );
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "soda";
    }

    @Override
    protected Component provideName() {
        return Component.text("Soda");
    }

    @Override
    protected String provideDescription() {
        return "Adds 30 seconds of adrenaline";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 3;
    }
}
