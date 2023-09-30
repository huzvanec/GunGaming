package cz.jeme.programu.gungaming.util.registry;

import cz.jeme.programu.gungaming.item.CustomItem;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Groups {

    public static @NotNull Map<String, Map<String, ? extends CustomItem>> groups = new HashMap<>();

    private Groups() {
        // Static class cannot be initialized
    }

    public static void register(@NotNull String groupName, @NotNull Map<String, ? extends CustomItem> map) {
        groups.put(groupName.replace(' ', '_').toLowerCase(), map);
    }

    public static void registered() {
        groups = Collections.unmodifiableMap(groups);
    }
}
