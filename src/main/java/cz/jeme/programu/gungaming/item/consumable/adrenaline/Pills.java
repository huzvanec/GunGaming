package cz.jeme.programu.gungaming.item.consumable.adrenaline;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class Pills extends Adrenaline {
    @Override
    public int getCustomModelData() {
        return 3;
    }

    @Override
    public @NotNull String getName() {
        return "Pills";
    }

    @Override
    public @NotNull String getInfo() {
        return "Adds 60 seconds of adrenaline";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public @NotNull Material getMaterial() {
        return Material.GOLDEN_APPLE;
    }

    @Override
    public int getMinStackLoot() {
        return 1;
    }

    @Override
    public int getMaxStackLoot() {
        return 2;
    }

    @Override
    protected @NotNull Set<PotionEffect> getEffects() {
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
}