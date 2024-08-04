package cz.jeme.programu.gungaming.item.melee.impl;

import cz.jeme.programu.gungaming.item.melee.Melee;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class DirtySword extends Melee {
    @Override
    protected @NotNull Component provideName() {
        return Component.text("Dirty Sword");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Poisons enemies on hit";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "dirty_sword";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
    }

    @Override
    protected double provideDamage() {
        return 10;
    }

    @Override
    protected void onHit(final @NotNull EntityDamageEvent event, final @NotNull ItemStack item) {
        if (!(event.getEntity() instanceof final LivingEntity livingEntity)) return;
        livingEntity.addPotionEffect(new PotionEffect(
                PotionEffectType.POISON,
                200,
                1,
                false,
                true,
                true
        ));
        super.onHit(event, item);
    }
}
