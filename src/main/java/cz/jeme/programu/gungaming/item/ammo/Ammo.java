package cz.jeme.programu.gungaming.item.ammo;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.CustomItem;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public abstract class Ammo extends CustomItem {
    public Ammo() {
        setup();

        Namespace.AMMO.set(item, name);
    }

    @Override
    protected @NotNull Material getMaterial() {
        return Material.WHITE_DYE;
    }
}