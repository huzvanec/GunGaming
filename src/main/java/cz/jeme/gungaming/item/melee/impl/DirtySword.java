package cz.jeme.gungaming.item.melee.impl;

import cz.jeme.gungaming.game.Game;
import cz.jeme.gungaming.game.GameTeam;
import cz.jeme.gungaming.item.melee.Sword;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DirtySword extends Sword {
    @Override
    protected Component provideName() {
        return Component.text("Dirty Sword");
    }

    @Override
    protected String provideDescription() {
        return "Poisons enemies on hit";
    }

    @Override
    protected Material provideMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "dirty_sword";
    }

    @Override
    protected double provideDamageBonus() {
        return 7;
    }

    @Override
    protected void onHit(final EntityDamageByEntityEvent event, final ItemStack item) {
        if (!(event.getEntity() instanceof final LivingEntity livingEntity)) return;
        if (
                Game.running() &&
                event.getDamager() instanceof final Player damager &&
                GameTeam.ofPlayer(damager).contains(livingEntity.getUniqueId())
        ) return; // don't damage teammates
        livingEntity.addPotionEffect(new PotionEffect(
                PotionEffectType.POISON,
                200,
                0,
                false,
                true,
                true
        ));
        super.onHit(event, item);
    }
}
