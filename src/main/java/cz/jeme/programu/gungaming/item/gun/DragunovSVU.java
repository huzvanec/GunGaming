package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.SevenSixTwoMm;
import cz.jeme.programu.gungaming.loot.Rarity;

public class DragunovSVU extends Gun {

    @Override
    protected void setup() {
        name = "Dragunov SVU";
        info = "AK-47 but it's a sniper rifle";
        shootCooldown = 900;
        reloadCooldown = 2050;
        damage = 10.5d;
        velocity = 60f;
        customModelData = 7;
        maxAmmo = 10;
        ammoType = SevenSixTwoMm.class;
        rarity = Rarity.EPIC;
        recoil = 0.22f;
        inaccuracy = 0.7f;
    }
}