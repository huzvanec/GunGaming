package cz.jeme.gungaming.item.gun;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NullMarked
public enum StatsMenuManager {
    INSTANCE;

    private final Map<UUID, StatsMenu> menus = new HashMap<>();

    public void
    createMenu(final HumanEntity player, final ItemStack gunItem) {
        menus.put(player.getUniqueId(), new StatsMenu(player, gunItem));
    }

    public @Nullable StatsMenu getMenu(final HumanEntity player) {
        return menus.get(player.getUniqueId());
    }

    public @Nullable StatsMenu removeMenu(final HumanEntity player) {
        return menus.remove(player.getUniqueId());
    }
}
