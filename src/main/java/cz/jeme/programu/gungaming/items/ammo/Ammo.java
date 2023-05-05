package cz.jeme.programu.gungaming.items.ammo;

import cz.jeme.programu.gungaming.items.CustomItem;

public abstract class Ammo extends CustomItem {
    public Integer minLoot = null;
    public Integer maxLoot = null;

    public Ammo() {
        setup();

        assert minLoot != null : "No minimal loot given!";
        assert maxLoot != null : "No maximal loot given!";
    }
}
