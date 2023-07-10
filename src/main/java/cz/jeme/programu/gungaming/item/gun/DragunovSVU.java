package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.SeventSixTwoMm;
import cz.jeme.programu.gungaming.loot.Rarity;

public class DragunovSVU extends Gun {

    @Override
    protected void setup() {
        name = "Dragunov SVU";
        info = "AK-47 but it's a sniper rifle";
        shootCooldown = 600;
        reloadCooldown = 3500;
        damage = 8d;
        velocity = 40f;
        customModelData = 7;
        maxAmmo = 10;
        ammoType = SeventSixTwoMm.class;
        rarity = Rarity.EPIC;
        recoil = 0.22f;
        inaccuracy = 0.7f;
    }
}