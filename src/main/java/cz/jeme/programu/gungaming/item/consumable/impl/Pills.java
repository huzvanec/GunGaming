package cz.jeme.programu.gungaming.item.consumable.impl;

import cz.jeme.programu.gungaming.item.consumable.Adrenaline;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Pills extends Adrenaline {
    @Override
    protected @NotNull Set<PotionEffect> provideEffects() {
        return Set.of(
                new PotionEffect(
                        PotionEffectType.REGENERATION,
                        1200,
                        0,
                        false,
                        true,
                        false
                ),
                new PotionEffect(
                        PotionEffectType.SPEED,
                        1200,
                        0,
                        false,
                        true,
                        false
                )
        );
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "pills";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Pills");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Adds 60 seconds of adrenaline";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 3;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 2;
    }
}
