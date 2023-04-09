package cz.jeme.programu.gungaming.items.ammo;

import org.bukkit.Material;

public class Rocket extends Ammo {

    @Override
    protected void setup() {
        material = Material.BLUE_DYE;
        name = "Rocket";
        loreLine = "Rocket ammo for Rocket Launcher";
    }

}
