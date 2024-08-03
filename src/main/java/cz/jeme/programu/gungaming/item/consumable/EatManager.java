package cz.jeme.programu.gungaming.item.consumable;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public enum EatManager {
    INSTANCE;

    private final @NotNull Map<UUID, Eating> soundBoard = new HashMap<>();

    public void startEating(final @NotNull Player player, final @NotNull EquipmentSlot hand) {
        soundBoard.put(player.getUniqueId(), new Eating(player, hand));
    }

    public void stopEating(final @NotNull Player player) {
        final UUID uuid = player.getUniqueId();
        final Eating eating = soundBoard.get(uuid);
        if (eating == null || eating.isCancelled()) return;
        eating.cancel();
        soundBoard.remove(uuid);
    }
}
