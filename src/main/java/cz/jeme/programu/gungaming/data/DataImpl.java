package cz.jeme.programu.gungaming.data;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

record DataImpl<P, C>(
        @NotNull NamespacedKey key,
        @NotNull PersistentDataType<P, C> type
) implements Data<P, C> {
    @Override
    public void write(final @NotNull PersistentDataContainer container, @NotNull final C value) {
        container.set(key, type, value);
    }

    @Override
    public void write(final @NotNull PersistentDataHolder holder, @NotNull final C value) {
        write(holder.getPersistentDataContainer(), value);
    }

    @Override
    public void write(final @NotNull ItemStack item, @NotNull final C value) {
        final ItemMeta meta = item.hasItemMeta()
                ? item.getItemMeta()
                : Bukkit.getItemFactory().getItemMeta(item.getType());
        write(meta, value);
        item.setItemMeta(meta);
    }

    @Override
    public @NotNull Optional<C> read(final @Nullable PersistentDataContainer container) {
        if (container == null) return Optional.empty();
        return Optional.ofNullable(container.get(key, type));
    }

    @Override
    public @NotNull Optional<C> read(final @Nullable PersistentDataHolder holder) {
        if (holder == null) return Optional.empty();
        return read(holder.getPersistentDataContainer());
    }

    @Override
    public @NotNull Optional<C> read(final @Nullable ItemStack item) {
        if (item == null || !item.hasItemMeta()) return Optional.empty();
        return read(item.getItemMeta());
    }

    @Override
    public @NotNull C require(final @NotNull PersistentDataContainer container) {
        final C value = container.get(key, type);
        if (value == null) throw new NullPointerException("The required data is not present!");
        return value;
    }

    @Override
    public @NotNull C require(final @NotNull PersistentDataHolder holder) {
        return require(holder.getPersistentDataContainer());
    }

    @Override
    public @NotNull C require(final @NotNull ItemStack item) {
        if (!item.hasItemMeta())
            throw new IllegalArgumentException("The provided item does not have ItemMeta!");
        return require(item.getItemMeta());
    }

    @Override
    public boolean check(final @Nullable PersistentDataContainer container) {
        if (container == null) return false;
        return container.has(key, type);
    }

    @Override
    public boolean check(final @Nullable PersistentDataHolder holder) {
        if (holder == null) return false;
        return check(holder.getPersistentDataContainer());
    }

    @Override
    public boolean check(final @Nullable ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return check(item.getItemMeta());
    }

    @Override
    public void delete(final @NotNull PersistentDataContainer container) {
        container.remove(key);
    }

    @Override
    public void delete(final @NotNull PersistentDataHolder holder) {
        delete(holder.getPersistentDataContainer());
    }

    @Override
    public void delete(final @NotNull ItemStack item) {
        if (!item.hasItemMeta()) return;
        item.editMeta(this::delete);
    }
}
