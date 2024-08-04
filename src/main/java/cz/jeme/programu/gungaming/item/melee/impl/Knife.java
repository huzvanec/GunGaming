package cz.jeme.programu.gungaming.item.melee.impl;

import cz.jeme.programu.gungaming.item.melee.Melee;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Knife extends Melee {
    @Override
    protected @NotNull Component provideName() {
        return Component.text("Knife");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Insanely fast melee weapon";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.NETHERITE_SWORD;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "knife";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
    }

    @Override
    protected double provideDamage() {
        return 6;
    }

    @Override
    protected double provideAttackSpeed() {
        return 994.9;
    }

    @Override
    protected void onHit(final @NotNull EntityDamageEvent event, final @NotNull ItemStack item) {
        if (!(event.getEntity() instanceof final LivingEntity livingEntity)) return;
        livingEntity.setMaximumNoDamageTicks(0);
    }
}