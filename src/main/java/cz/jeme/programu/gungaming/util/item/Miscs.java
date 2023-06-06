package cz.jeme.programu.gungaming.util.item;

import cz.jeme.programu.gungaming.item.misc.Misc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Miscs {
    public static Map<String, Misc> miscs = new HashMap<>();

    private Miscs() {}

    public static Misc getMisc(String name) {
        return miscs.get(name);
    }

    public static void register(Misc misc) {
        miscs.put(misc.name, misc);
    }

    public static void setUnmodifiable() {
        miscs = Collections.unmodifiableMap(miscs);
    }
}
