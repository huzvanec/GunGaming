package cz.jeme.gungaming.item.consumable.impl;

import cz.jeme.gungaming.item.consumable.Adrenaline;
import cz.jeme.gungaming.loot.Rarity;
import io.papermc.paper.datacomponent.item.Consumable;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;

import java.util.Set;

@NullMarked
public class Pills extends Adrenaline {
    @Override
    protected Set<PotionEffect> provideEffects() {
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

    @SuppressWarnings("UnstableApiUsage")
    @Override
    protected void buildConsumable(final Consumable.Builder builder) {
        builder.consumeSeconds(3);
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "pills";
    }

    @Override
    protected Component provideName() {
        return Component.text("Pills");
    }

    @Override
    protected String provideDescription() {
        return "Adds 60 seconds of adrenaline";
    }

    @Override
    protected Rarity provideRarity() {
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
