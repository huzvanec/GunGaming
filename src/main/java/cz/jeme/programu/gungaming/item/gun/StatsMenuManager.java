package cz.jeme.programu.gungaming.item.gun;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public enum StatsMenuManager {
    INSTANCE;

    private final @NotNull Map<UUID, StatsMenu> menus = new HashMap<>();

    public void
    createMenu(final @NotNull HumanEntity player, final @NotNull ItemStack gunItem) {
        menus.put(player.getUniqueId(), new StatsMenu(player, gunItem));
    }

    public @Nullable StatsMenu getMenu(final @NotNull HumanEntity player) {
        return menus.get(player.getUniqueId());
    }

    public @Nullable StatsMenu removeMenu(final @NotNull HumanEntity player) {
        return menus.remove(player.getUniqueId());
    }
}
