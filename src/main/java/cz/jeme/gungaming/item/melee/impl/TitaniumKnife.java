package cz.jeme.gungaming.item.melee.impl;

import cz.jeme.gungaming.item.melee.Melee;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TitaniumKnife extends Melee {
    @Override
    protected Component provideName() {
        return Component.text("Titanium Knife");
    }

    @Override
    protected String provideDescription() {
        return "Insanely fast titanium weapon";
    }

    @Override
    protected Material provideMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "titanium_knife";
    }

    @Override
    protected double provideDamageBonus() {
        return 3;
    }

    @Override
    protected double provideAttackSpeedBonus() {
        return 994.9;
    }

    @Override
    protected void onHit(final EntityDamageEvent event, final ItemStack item) {
        if (!(event.getEntity() instanceof final LivingEntity livingEntity)) return;
        livingEntity.setMaximumNoDamageTicks(0);
    }
}