package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.TwelveGauge;
import cz.jeme.programu.gungaming.loot.Rarity;

public class RemingtonM870 extends Gun implements Magazineless {

    @Override
    protected void setup() {
        name = "Remington M870";
        info = "a common shotgun";
        shootCooldown = 1250;
        reloadCooldown = 660;
        damage = 2.3d;
        velocity = 40f;
        maxAmmo = 5;
        ammoType = TwelveGauge.class;
        customModelData = 6;
        rarity = Rarity.RARE;
        recoil = 0.05f;
        inaccuracy = 3f;
        bulletsPerShot = 9;
        bulletCooldown = 0;
    }
}