package cz.jeme.programu.gungaming.eventhandler;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerHealHandler {
    private final @NotNull Map<UUID, Integer> healing = new HashMap<>();
    public void onFoodLevelChange(@NotNull FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
    public void onEntityRegainHealth(@NotNull EntityRegainHealthEvent event) {
        if (event.getRegainReason() != EntityRegainHealthEvent.RegainReason.SATIATED) return;
        if (!(event.getEntity() instanceof Player player)) return;

        UUID uuid = player.getUniqueId();

        if (!healing.containsKey(uuid)) {
            event.setCancelled(true);
            healing.put(uuid, 1);
            return;
        }

        if (healing.get(uuid) == 4) {
            healing.put(uuid, 0);
            return;
        }

        event.setCancelled(true);
        healing.put(uuid, healing.get(uuid) + 1);
    }
}
