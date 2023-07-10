package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.SeventSixTwoMm;
import cz.jeme.programu.gungaming.loot.Rarity;

public class AK47 extends Gun {

    @Override
    protected void setup() {
        name = "AK-47";
        info = "pretty good assault rifle";
        shootCooldown = 200;
        reloadCooldown = 2500;
        damage = 2.5d;
        velocity = 40f;
        maxAmmo = 30;
        ammoType = SeventSixTwoMm.class;
        customModelData = 3;
        rarity = Rarity.EPIC;
        recoil = 0.08f;
        inaccuracy = 1.7f;
    }
}