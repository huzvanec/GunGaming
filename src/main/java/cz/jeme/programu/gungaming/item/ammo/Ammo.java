package cz.jeme.programu.gungaming.item.ammo;

import cz.jeme.programu.gungaming.item.CustomItem;
import org.bukkit.Material;

public abstract class Ammo extends CustomItem {
    @Override
    protected Material getMaterial() {
        return Material.WHITE_DYE;
    }
}