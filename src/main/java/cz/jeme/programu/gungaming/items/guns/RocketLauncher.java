package cz.jeme.programu.gungaming.items.guns;

import org.bukkit.Material;

public class RocketLauncher extends Gun {

    @Override
    protected void setup() {
        name = "Rocket Launcher";
        loreLine = "Awesome launchers";
        shootCooldown = 2000;
        reloadCooldown = 7000;
        damage = 15d;
        velocity = 1.5f;
        material = Material.DIAMOND_HOE;
        maxAmmo = 1;
        isRocket = true;
        ammoName = "Rocket";
    }
}