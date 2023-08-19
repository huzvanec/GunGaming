package cz.jeme.programu.gungaming.item.consumable;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.jetbrains.annotations.NotNull;

public abstract class Heal extends Consumable {
    public Double heal = null;
    public Heal() {
        setup();

        assert heal != null : "Heal amount not given!";
    }

    @Override
    protected final void onConsume(@NotNull PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        double health = player.getHealth() + heal;
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert attribute != null : "Max health attribute is null!";
        health = Math.min(health, attribute.getBaseValue());
        player.setHealth(health);
    }
}
