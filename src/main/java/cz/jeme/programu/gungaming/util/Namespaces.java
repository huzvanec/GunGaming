package cz.jeme.programu.gungaming.util;

import cz.jeme.programu.gungaming.GunGaming;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public enum Namespaces {
    RARITY(GunGaming.namespacedKey("rarity"), PersistentDataType.STRING),
    INFO(GunGaming.namespacedKey("info"), PersistentDataType.STRING),
    // Gun namespaces
    GUN(GunGaming.namespacedKey("gun"), PersistentDataType.STRING),
    CURRENT_GUN_AMMO(GunGaming.namespacedKey("current_gun_ammo"), PersistentDataType.INTEGER),
    MAX_GUN_AMMO(GunGaming.namespacedKey("max_gun_ammo"), PersistentDataType.INTEGER),
    GUN_SCOPE(GunGaming.namespacedKey("gun_scope"), PersistentDataType.DOUBLE),

    // Ammo namespaces
    AMMO(GunGaming.namespacedKey("ammo"), PersistentDataType.STRING),

    // Bullet (entity) namespaces
    BULLET(GunGaming.namespacedKey("bullet"), PersistentDataType.STRING),
    BULLET_DAMAGE(GunGaming.namespacedKey("bullet_damage"), PersistentDataType.DOUBLE),
    BULLET_GUN_NAME(GunGaming.namespacedKey("bullet_gun_name"), PersistentDataType.STRING),

    // Misc namespaces
    MISC(GunGaming.namespacedKey("misc"), PersistentDataType.STRING);

    public final NamespacedKey namespacedKey;
    public final String key;
    public final PersistentDataType<?, ?> type;

    Namespaces(NamespacedKey namespacedKey, PersistentDataType<?, ?> type) {
        this.namespacedKey = namespacedKey;
        this.key = namespacedKey.getKey();
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public <T, Z> void set(PersistentDataHolder holder, @NotNull Z value) {
        if (holder == null) throw new IllegalArgumentException("Holder is null!");
        holder.getPersistentDataContainer().set(namespacedKey, (PersistentDataType<T, Z>) type, value);
    }

    public <Z> void set(@NotNull ItemStack item, @NotNull Z value) {
        ItemMeta meta = item.getItemMeta();
        set(meta, value);
        item.setItemMeta(meta);
    }

    @SuppressWarnings("unchecked")
    public <T, Z> Z get(PersistentDataHolder holder) {
        if (holder == null) throw new IllegalArgumentException("Holder is null!");
        return holder.getPersistentDataContainer().get(namespacedKey, (PersistentDataType<T, Z>) type);
    }

    public <Z> Z get(@NotNull ItemStack item) {
        return get(item.getItemMeta());
    }

    public boolean has(PersistentDataHolder holder) {
        if (holder == null) throw new IllegalArgumentException("Holder is null!");
        return holder.getPersistentDataContainer().has(namespacedKey);
    }

    public boolean has(@NotNull ItemStack item) {
        return has(item.getItemMeta());
    }
}
