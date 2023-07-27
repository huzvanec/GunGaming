package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.SevenSixTwoMm;
import cz.jeme.programu.gungaming.loot.Rarity;

public class AK47 extends Gun {

    @Override
    protected void setup() {
        name = "AK-47";
        info = "pretty good assault rifle";
        shootCooldown = 200;
        reloadCooldown = 2250;
        damage = 1.2d;
        velocity = 40f;
        maxAmmo = 30;
        ammoType = SevenSixTwoMm.class;
        customModelData = 3;
        rarity = Rarity.EPIC;
        recoil = 0.05f;
        inaccuracy = 1.7f;
        bulletsPerShot = 2;
        bulletCooldown = 2;
    }
}