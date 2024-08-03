package cz.jeme.programu.gungaming.item.attachment;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public enum AttachmentMenuManager {
    INSTANCE;

    private final @NotNull Map<UUID, AttachmentMenu> menus = new HashMap<>();

    public void
    createMenu(final @NotNull HumanEntity player, final @NotNull ItemStack gunItem) {
        menus.put(player.getUniqueId(), new AttachmentMenu(player, gunItem));
    }

    public @Nullable AttachmentMenu getMenu(final @NotNull HumanEntity player) {
        return menus.get(player.getUniqueId());
    }

    public @Nullable AttachmentMenu removeMenu(final @NotNull HumanEntity player) {
        return menus.remove(player.getUniqueId());
    }
}
