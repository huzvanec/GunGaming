package cz.jeme.programu.gungaming.items.guns;

import org.bukkit.Material;

public class M9 extends Gun {

    @Override
    protected void setup() {
        name = "M9";
        loreLine = "Basic pistol";
        shootCooldown = 400;
        reloadCooldown = 1000;
        damage = 4d;
        velocity = 40f;
        material = Material.WOODEN_HOE;
        maxAmmo = 15;
        isRocket = false;
        ammoName = "9mm";
    }
}