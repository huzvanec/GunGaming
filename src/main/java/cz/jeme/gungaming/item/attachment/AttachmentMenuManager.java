package cz.jeme.gungaming.item.attachment;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NullMarked
public enum AttachmentMenuManager {
    INSTANCE;

    private final Map<UUID, AttachmentMenu> menus = new HashMap<>();

    public void
    createMenu(final HumanEntity player, final ItemStack gunItem) {
        menus.put(player.getUniqueId(), new AttachmentMenu(player, gunItem));
    }

    public @Nullable AttachmentMenu getMenu(final HumanEntity player) {
        return menus.get(player.getUniqueId());
    }

    public @Nullable AttachmentMenu removeMenu(final HumanEntity player) {
        return menus.remove(player.getUniqueId());
    }
}
