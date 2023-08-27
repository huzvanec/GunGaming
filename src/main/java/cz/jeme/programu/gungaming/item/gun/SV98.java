package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.SevenSixTwoMm;
import cz.jeme.programu.gungaming.loot.Rarity;

public class SV98 extends Gun {

    @Override
    protected void setup() {
        name = "SV-98";
        info = "A powerfull sniper rifle";
        shootCooldown = 1400;
        reloadCooldown = 2400;
        damage = 20d;
        velocity = 80f;
        customModelData = 8;
        maxAmmo = 10;
        ammoType = SevenSixTwoMm.class;
        rarity = Rarity.LEGENDARY;
        recoil = 0.33f;
        inaccuracy = 0.4f;
    }
}