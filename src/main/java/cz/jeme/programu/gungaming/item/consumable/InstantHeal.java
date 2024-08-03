package cz.jeme.programu.gungaming.item.consumable;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.jetbrains.annotations.NotNull;

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
    protected void onConsume(final @NotNull PlayerItemConsumeEvent event) {
        final Player player = event.getPlayer();
        final double health = player.getHealth() + healAmount;
        final AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        final double maxHealth = attribute == null ? health : attribute.getValue();
        player.setHealth(Math.min(maxHealth, health));
    }

    @Override
    protected void onUse(final @NotNull PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final double health = player.getHealth();
        final AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        final double maxHealth = attribute == null ? health : attribute.getValue();
        if (health >= maxHealth) {
            event.setCancelled(true);
            return;
        }
        super.onUse(event);
    }
}
