package cz.jeme.gungaming.persistence;

import io.papermc.paper.persistence.PersistentDataContainerView;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

@NullMarked
record PersistentDataImpl<P, C>(
        NamespacedKey key,
        PersistentDataType<P, C> type
) implements PersistentData<P, C> {
    @Override
    public void write(final PersistentDataContainer container, final C value) {
        container.set(key, type, value);
    }

    @Override
    public Optional<C> read(final @Nullable PersistentDataContainerView container) {
        if (container == null) return Optional.empty();
        return Optional.ofNullable(container.get(key, type));
    }

    @Override
    public C require(final PersistentDataContainerView container) {
        final C value = container.get(key, type);
        if (value == null)
            throw new IllegalArgumentException("Missing value for key '" + key + "'");
        return value;
    }

    @Override
    public boolean check(final @Nullable PersistentDataContainerView container) {
        return container != null && container.has(key, type);
    }

    @Override
    public void delete(final PersistentDataContainer container) {
        container.remove(key);
    }
}
