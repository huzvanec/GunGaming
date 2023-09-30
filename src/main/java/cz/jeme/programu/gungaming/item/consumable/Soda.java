package cz.jeme.programu.gungaming.item.consumable;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Soda extends Adrenaline {

    @Override
    protected void setup() {
        name = "Soda";
        info = "adds 30 seconds of adrenaline";
        customModelData = 2;
        rarity = Rarity.RARE;
    }

    @Override
    protected @NotNull Material getMaterial() {
        return Material.GOLDEN_APPLE;
    }

    @Override
    public int getMinLoot() {
        return 1;
    }

    @Override
    public int getMaxLoot() {
        return 3;
    }

    @Override
    @NotNull
    List<PotionEffect> getEffects() {
        return List.of(
                new PotionEffect(PotionEffectType.REGENERATION, 600, 0, false, true, false),
                new PotionEffect(PotionEffectType.SPEED, 600, 0, false, true, false)
        );
    }
}
