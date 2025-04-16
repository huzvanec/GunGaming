package cz.jeme.gungaming.item.consumable;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ConsumableEventHandler {
    private ConsumableEventHandler() {
        throw new AssertionError();
    }

    public static void onPlayerItemConsume(final PlayerItemConsumeEvent event) {
        final ItemStack item = event.getItem();
        if (!Consumable.is(item)) return;
        final Player player = event.getPlayer();
        final Consumable consumable = Consumable.of(item);
        consumable.consume(event);
        player.getWorld().playSound(consumable.burpSound(item), player);
    }
}
