package cz.jeme.programu.gungaming.item.consumable;

import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ConsumableEventHandler {
    private ConsumableEventHandler() {
        throw new AssertionError();
    }

    public static void onPlayerItemConsume(final @NotNull PlayerItemConsumeEvent event) {
        final ItemStack item = event.getItem();
        if (!Consumable.is(item)) return;
        final Player player = event.getPlayer();
        EatManager.INSTANCE.stopEating(player);
        final Consumable consumable = Consumable.of(item);
        consumable.consume(event);
        player.getWorld().playSound(consumable.burpSound(item), player);
    }


    public static void onPlayerStopUsingItem(final @NotNull PlayerStopUsingItemEvent event) {
        if (!Consumable.is(event.getItem())) return;
        EatManager.INSTANCE.stopEating(event.getPlayer());
    }
}
