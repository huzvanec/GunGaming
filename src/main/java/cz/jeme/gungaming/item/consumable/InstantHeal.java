package cz.jeme.gungaming.item.consumable;

import cz.jeme.gungaming.util.Components;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class InstantHeal extends Consumable {
    protected double healAmount = provideHealAmount();

    protected InstantHeal() {
        addTags("heal");
    }

    // providers

    protected abstract double provideHealAmount();

    // getters

    public final double healAmount() {
        return healAmount;
    }

    // healing

    @Override
    protected void onConsume(final PlayerItemConsumeEvent event) {
        final Player player = event.getPlayer();
        final double health = player.getHealth() + healAmount;
        final AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        final double maxHealth = attribute == null ? health : attribute.getValue();
        player.setHealth(Math.min(maxHealth, health));
    }

    @Override
    protected void onUse(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final double health = player.getHealth();
        final AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        final double maxHealth = attribute == null ? health : attribute.getValue();
        if (health >= maxHealth) {
            event.setCancelled(true);
            player.sendActionBar(Components.of("<red>You are at full health!"));
            return;
        }
        super.onUse(event);
    }
}
