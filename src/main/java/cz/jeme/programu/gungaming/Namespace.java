package cz.jeme.programu.gungaming;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum Namespace {
    GG(PersistentDataType.BOOLEAN),
    RARITY(PersistentDataType.STRING),
    INFO(PersistentDataType.STRING),
    // Gun namespaces
    GUN(PersistentDataType.STRING),
    GUN_AMMO_CURRENT(PersistentDataType.INTEGER),
    GUN_AMMO_MAX(PersistentDataType.INTEGER),
    GUN_RELOAD_COOLDOWN(PersistentDataType.INTEGER),
    GUN_SCOPE(PersistentDataType.STRING),
    GUN_MAGAZINE(PersistentDataType.STRING),
    GUN_STOCK(PersistentDataType.STRING),
    GUN_RECOIL(PersistentDataType.FLOAT),
    GUN_INACCURACY(PersistentDataType.FLOAT),

    // Ammo namespaces
    AMMO(PersistentDataType.STRING),

    // Bullet (entity ammo) namespaces
    BULLET(PersistentDataType.STRING),
    BULLET_DAMAGE(PersistentDataType.DOUBLE),
    BULLET_GUN_NAME(PersistentDataType.STRING),

    // Misc namespaces
    MISC(PersistentDataType.STRING),
    // Attachment namespaces
    ATTACHMENT(PersistentDataType.STRING),
    // Throwable namespaces
    THROWABLE(PersistentDataType.STRING),
    // Thrown (entity throwable) namespaces
    THROWN(PersistentDataType.STRING),
    THROWN_DAMAGE(PersistentDataType.DOUBLE),
    // Consumable namespaces
    CONSUMABLE(PersistentDataType.STRING),
    // Grapling Hook namespaces
    HOOKED(PersistentDataType.BOOLEAN);

    public final @NotNull NamespacedKey namespacedKey;
    public final @NotNull PersistentDataType<?, ?> type;

    <T, Z> Namespace(@NotNull PersistentDataType<T, Z> type) {
        namespacedKey = GunGaming.generateNamespacedKey(name().toLowerCase());
        this.type = type;
    }

    <T, Z> Namespace(@NotNull PersistentDataType<T, Z> type, @NotNull NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
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
    public <T, Z> @Nullable Z get(@Nullable PersistentDataHolder holder) {
        if (holder == null) return null;
        return holder.getPersistentDataContainer().get(namespacedKey, (PersistentDataType<T, Z>) type);
    }

    public <T> @Nullable T get(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        return get(meta);
    }

    public boolean has(@Nullable PersistentDataHolder holder) {
        if (holder == null) return false;
        return holder.getPersistentDataContainer().has(namespacedKey);
    }

    public boolean has(@Nullable ItemStack item) {
        if (item == null) return false;
        return has(item.getItemMeta());
    }
}
