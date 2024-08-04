package cz.jeme.programu.gungaming.loot.crate;

import cz.jeme.programu.gungaming.item.CustomItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class CrateFilter {
    private static final @NotNull CrateFilter EMPTY = new CrateFilter(CrateFilterType.BLACKLIST);
    private final @NotNull CrateFilterType type;
    private final @NotNull Set<Class<? extends CustomItem>> content = new HashSet<>();

    public CrateFilter(final @NotNull CrateFilterType type) {
        this.type = type;
    }

    public CrateFilter(final @NotNull CrateFilter filter) {
        this(filter.type);
        content.addAll(filter.content);
    }

    @SafeVarargs
    public final @NotNull CrateFilter add(final @NotNull Class<? extends CustomItem> first, final Class<? extends CustomItem> @NotNull ... other) {
        content.add(first);
        content.addAll(Arrays.asList(other));
        return this;
    }

    public @NotNull CrateFilter remove(final @NotNull Class<? extends CustomItem> clazz) {
        content.remove(clazz);
        return this;
    }

    public @NotNull CrateFilter clear() {
        content.clear();
        return this;
    }

    public boolean check(final @NotNull CustomItem customItem) {
        final Class<? extends CustomItem> itemClass = customItem.getClass();
        final boolean inContent = content.stream().anyMatch(clazz -> clazz.isAssignableFrom(itemClass));
        return switch (type) {
            case BLACKLIST -> !inContent;
            case WHITELIST -> inContent;
        };
    }

    public @NotNull CrateFilterType type() {
        return type;
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof final CrateFilter filter)) return false;

        return type == filter.type && content.equals(filter.content);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + content.hashCode();
        return result;
    }


    public enum CrateFilterType {
        WHITELIST,
        BLACKLIST
    }


    public static @NotNull CrateFilter empty() {
        return EMPTY;
    }
}
