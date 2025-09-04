package cz.jeme.gungaming;

import cz.jeme.gungaming.game.Game;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.util.Components;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.command.UnknownCommandEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class GlobalEventHandler {
    private GlobalEventHandler() {
        throw new AssertionError();
    }

    public static void onEntityDamage(final EntityDamageEvent event) {
        switch (event.getCause()) {
            case PROJECTILE, ENTITY_ATTACK, ENTITY_SWEEP_ATTACK -> {
            }
            default -> resetNoDamageTicks(event.getEntity());
        }
    }

    public static void resetNoDamageTicks(final Entity entity) {
        if (!(entity instanceof final LivingEntity livingEntity)) return;
        livingEntity.setMaximumNoDamageTicks(20);
    }

    public static void onUnknownCommand(final UnknownCommandEvent event) {
        final String[] cmd = event.getCommandLine().split(" ");
        if (!cmd[0].equals("_ggdev")) return;
        if (!(event.getSender() instanceof final Player player)) {
            event.getSender().sendMessage(Components.prefix(
                    "<red>Only players can use GunGaming developer commands!"
            ));
            return;
        }
        if (!player.hasPermission("gungaming.developer")) return;
        event.message(null);
        switch (cmd[1]) {
            case "all_items" -> {
                for (final CustomItem item : ElementManager.INSTANCE.items()) {
                    player.getWorld().dropItem(
                            player.getLocation(),
                            item.createStack()
                    );
                }
                player.sendMessage(Components.prefix("<gold>Dropped all GunGaming items on the ground"));
            }
            case "single_team_start" -> {
                Game.singleTeamStart = true;
                player.sendMessage(Components.prefix("<gold>Games may now start with a single team"));
            }
        }
    }
}
