package cz.jeme.gungaming.loot.crate;

import cz.jeme.gungaming.item.CustomItem;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@NullMarked
public final class CrateFilter {
    private static final CrateFilter EMPTY = new CrateFilter(CrateFilterType.BLACKLIST);
    private final CrateFilterType type;
    private final Set<Class<? extends CustomItem>> content = new HashSet<>();

    public CrateFilter(final CrateFilterType type) {
        this.type = type;
    }

    public CrateFilter(final CrateFilter filter) {
        this(filter.type);
        content.addAll(filter.content);
    }

    @SafeVarargs
    public final CrateFilter add(final Class<? extends CustomItem> first, final Class<? extends CustomItem>... other) {
        content.add(first);
        content.addAll(Arrays.asList(other));
        return this;
    }

    public CrateFilter remove(final Class<? extends CustomItem> clazz) {
        content.remove(clazz);
        return this;
    }

    public CrateFilter clear() {
        content.clear();
        return this;
    }

    public boolean check(final CustomItem customItem) {
        final Class<? extends CustomItem> itemClass = customItem.getClass();
        final boolean inContent = content.stream().anyMatch(clazz -> clazz.isAssignableFrom(itemClass));
        return switch (type) {
            case BLACKLIST -> !inContent;
            case WHITELIST -> inContent;
        };
    }

    public CrateFilterType type() {
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


    public static CrateFilter empty() {
        return EMPTY;
    }
}
