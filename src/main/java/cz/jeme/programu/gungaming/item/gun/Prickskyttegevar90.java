package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.ThreeZeroEightSubsonicWinchester;
import cz.jeme.programu.gungaming.loot.Rarity;

public class Prickskyttegevar90 extends Gun {

    @Override
    protected void setup() {
        name = "Prickskyttegevar 90";
        displayName = "Prickskyttegevär 90";
        info = "The best sniper rifle";
        shootCooldown = 1600;
        reloadCooldown = 3000;
        damage = 30d;
        velocity = 30f;
        customModelData = 9;
        maxAmmo = 10;
        ammoType = ThreeZeroEightSubsonicWinchester.class;
        rarity = Rarity.LEGENDARY;
        recoil = 0.2f;
        inaccuracy = 0.6f;
    }
}