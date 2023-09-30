package cz.jeme.programu.gungaming.eventhandler;

import cz.jeme.programu.gungaming.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerHealHandler {
    private static final @NotNull Map<UUID, Integer> HEALING = new HashMap<>();
    private PlayerHealHandler() {
        throw new AssertionError();
    }

    public static void onFoodLevelChange(@NotNull FoodLevelChangeEvent event) {
        if (!Game.isRunning()) return;
        event.setCancelled(true);
    }
    public static void onEntityRegainHealth(@NotNull EntityRegainHealthEvent event) {
        if (event.getRegainReason() != EntityRegainHealthEvent.RegainReason.SATIATED) return;
        if (!(event.getEntity() instanceof Player player)) return;
        if (!Game.isRunning()) return;

        UUID uuid = player.getUniqueId();

        if (!HEALING.containsKey(uuid)) {
            event.setCancelled(true);
            HEALING.put(uuid, 1);
            return;
        }

        if (HEALING.get(uuid) == 4) {
            HEALING.put(uuid, 0);
            return;
        }

        event.setCancelled(true);
        HEALING.put(uuid, HEALING.get(uuid) + 1);
    }
}
