package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.NineMm;
import cz.jeme.programu.gungaming.loot.Rarity;

public class M9 extends Gun {

    @Override
    protected void setup() {
        name = "M9";
        info = "A Basic pistol";
        shootCooldown = 400;
        reloadCooldown = 1550;
        damage = 3d;
        velocity = 40f;
        customModelData = 1;
        maxAmmo = 15;
        ammoType = NineMm.class;
        rarity = Rarity.RARE;
        recoil = 0.1f;
        inaccuracy = 1.2f;
    }
}