package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.loot.Rarity;

public class NagantM1895 extends Gun {

    @Override
    protected void setup() {
        name = "Nagant M1895";
        info = "A lousy revolver";
        shootCooldown = 530;
        reloadCooldown = 1500;
        damage = 4d;
        velocity = 40f;
        customModelData = 2;
        maxAmmo = 7;
        ammoName = "7.62mm";
        rarity = Rarity.RARE;
        recoil = 0.18f;
        inaccuracy = 0.9f;
    }
}