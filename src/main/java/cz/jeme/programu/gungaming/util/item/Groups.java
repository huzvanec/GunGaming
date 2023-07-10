package cz.jeme.programu.gungaming.util.item;

import cz.jeme.programu.gungaming.item.CustomItem;

import java.util.*;

public final class Groups {

    public static Map<String, Map<String, ? extends CustomItem>> groups = new HashMap<>();

    private Groups() {
        // Static class cannot be initialized
    }

    public static void register(String groupName, Map<String, ? extends CustomItem> map) {
        groups.put(groupName.replace(' ', '_').toLowerCase(), map);
    }

    public static void setUnmodifiable() {
        groups = Collections.unmodifiableMap(groups);
    }
}
