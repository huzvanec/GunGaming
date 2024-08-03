package cz.jeme.programu.gungaming.item.consumable.impl;

import cz.jeme.programu.gungaming.item.consumable.Adrenaline;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Soda extends Adrenaline {
    @Override
    protected @NotNull Set<PotionEffect> provideEffects() {
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
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "soda";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Soda");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Adds 30 seconds of adrenaline";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 2;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
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
