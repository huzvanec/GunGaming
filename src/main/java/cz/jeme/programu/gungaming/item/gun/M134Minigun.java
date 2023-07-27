package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.SevenSixTwoMm;
import cz.jeme.programu.gungaming.item.attachment.NoMagazine;
import cz.jeme.programu.gungaming.item.attachment.NoScope;
import cz.jeme.programu.gungaming.item.attachment.NoStock;
import cz.jeme.programu.gungaming.loot.Rarity;

public class M134Minigun extends Gun implements NoMagazine, NoStock, NoScope {

    @Override
    protected void setup() {
        name = "M134 Minigun";
        info = "A legendary gatling-like rotary minigun";
        shootCooldown = 200;
        reloadCooldown = 4900;
        damage = 2.5d;
        velocity = 40f;
        maxAmmo = 200;
        ammoType = SevenSixTwoMm.class;
        customModelData = 5;
        rarity = Rarity.LEGENDARY;
        recoil = 0.05f;
        inaccuracy = 1.3f;
        bulletsPerShot = 2;
        bulletCooldown = 2;
    }
}