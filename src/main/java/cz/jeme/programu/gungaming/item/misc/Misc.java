package cz.jeme.programu.gungaming.item.misc;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.CustomItem;

public abstract class Misc extends CustomItem {
    public Misc() {
        setup();
        Namespace.MISC.set(item, name);
    }
}
