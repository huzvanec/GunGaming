package cz.jeme.gungaming.item.melee;

import cz.jeme.gungaming.GlobalEventHandler;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.item.melee.impl.Fish;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MeleeEventHandler {
    private MeleeEventHandler() {
        throw new AssertionError();
    }

    public static void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        final EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK && cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)
            return;
        final Entity attacked = event.getEntity();
        if (!(event.getDamager() instanceof final Player player)) {
            GlobalEventHandler.resetNoDamageTicks(attacked);
            return;
        }

        final ItemStack item = player.getInventory().getItemInMainHand();

        if (!Melee.is(item)) {
            GlobalEventHandler.resetNoDamageTicks(attacked);
            return;
        }
        Melee.of(item).onHit(event, item);
    }

    public static void onPlayerItemConsume(final PlayerItemConsumeEvent event) {
        if (!CustomItem.is(event.getItem(), Fish.class)) return;
        event.setCancelled(true);
        event.getPlayer().setHealth(0.1);
    }
}
