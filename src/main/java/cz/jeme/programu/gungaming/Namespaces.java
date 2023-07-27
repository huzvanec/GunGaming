package cz.jeme.programu.gungaming;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public enum Namespaces {
    GG(GunGaming.namespacedKey("gg"), PersistentDataType.BOOLEAN),
    RARITY(GunGaming.namespacedKey("rarity"), PersistentDataType.STRING),
    INFO(GunGaming.namespacedKey("info"), PersistentDataType.STRING),
    // Gun namespaces
    GUN(GunGaming.namespacedKey("gun"), PersistentDataType.STRING),
    GUN_AMMO_CURRENT(GunGaming.namespacedKey("current_gun_ammo"), PersistentDataType.INTEGER),
    GUN_AMMO_MAX(GunGaming.namespacedKey("max_gun_ammo"), PersistentDataType.INTEGER),
    GUN_RELOAD_COOLDOWN(GunGaming.namespacedKey("gun_reload_cooldown"), PersistentDataType.INTEGER),
    GUN_SCOPE(GunGaming.namespacedKey("gun_scope"), PersistentDataType.STRING),
    GUN_MAGAZINE(GunGaming.namespacedKey("gun_magazine"), PersistentDataType.STRING),
    GUN_STOCK(GunGaming.namespacedKey("gun_stock"), PersistentDataType.STRING),
    GUN_RECOIL(GunGaming.namespacedKey("gun_recoil"), PersistentDataType.FLOAT),
    GUN_INACCURACY(GunGaming.namespacedKey("gun_accuracy"), PersistentDataType.FLOAT),

    // Ammo namespaces
    AMMO(GunGaming.namespacedKey("ammo"), PersistentDataType.STRING),

    // Bullet (entity ammo) namespaces
    BULLET(GunGaming.namespacedKey("bullet"), PersistentDataType.STRING),
    BULLET_DAMAGE(GunGaming.namespacedKey("bullet_damage"), PersistentDataType.DOUBLE),
    BULLET_GUN_NAME(GunGaming.namespacedKey("bullet_gun_name"), PersistentDataType.STRING),

    // Misc namespaces
    MISC(GunGaming.namespacedKey("misc"), PersistentDataType.STRING),
    // Attachment namespaces
    ATTACHMENT(GunGaming.namespacedKey("attachment"), PersistentDataType.STRING),
    // Throwable namespaces
    THROWABLE(GunGaming.namespacedKey("throwable"), PersistentDataType.STRING),
    // Thrown (entity throwable) namespaces
    THROWN(GunGaming.namespacedKey("thrown"), PersistentDataType.STRING),
    THROWN_DAMAGE(GunGaming.namespacedKey("thrown_damage"), PersistentDataType.DOUBLE);

    public final NamespacedKey namespacedKey;
    public final String key;
    public final PersistentDataType<?, ?> type;

    Namespaces(NamespacedKey namespacedKey, PersistentDataType<?, ?> type) {
        this.namespacedKey = namespacedKey;
        this.key = namespacedKey.getKey();
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public <T, Z> void set(@NotNull PersistentDataHolder holder, @NotNull Z value) {
        holder.getPersistentDataContainer().set(namespacedKey, (PersistentDataType<T, Z>) type, value);
    }

    public <Z> void set(@NotNull ItemStack item, @NotNull Z value) {
        ItemMeta meta = item.getItemMeta();
        set(meta, value);
        item.setItemMeta(meta);
    }

    @SuppressWarnings("unchecked")
    public <T, Z> Z get(PersistentDataHolder holder) {
        if (holder == null) return null;
        return holder.getPersistentDataContainer().get(namespacedKey, (PersistentDataType<T, Z>) type);
    }

    public <Z> Z get(@NotNull ItemStack item) {
        return get(item.getItemMeta());
    }

    public boolean has(PersistentDataHolder holder) {
        if (holder == null) return false;
        return holder.getPersistentDataContainer().has(namespacedKey);
    }

    public boolean has(@NotNull ItemStack item) {
        return has(item.getItemMeta());
    }
}
