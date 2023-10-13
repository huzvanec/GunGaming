package cz.jeme.programu.gungaming.item.ammo;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.CustomItem;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public abstract class Ammo extends CustomItem {
    @Override
    public final @NotNull Material getMaterial() {
        return Material.WHITE_DYE;
    }

    public Ammo() {
        Namespace.AMMO.set(item, getName());
    }
}