package cz.jeme.programu.gungaming.item.consumable.heal;

import cz.jeme.programu.gungaming.item.consumable.Consumable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.jetbrains.annotations.NotNull;

public abstract class Heal extends Consumable {
    public abstract double getHealAmount();

    @Override
    protected final void onConsume(@NotNull PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        double health = player.getHealth() + getHealAmount();
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert attribute != null : "Max health attribute is null!";
        health = Math.min(health, attribute.getBaseValue());
        player.setHealth(health);
    }
}