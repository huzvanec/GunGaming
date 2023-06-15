package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class M9 extends Gun {

    @Override
    protected void setup() {
        name = "M9";
        info = "A Basic pistol";
        shootCooldown = 400;
        reloadCooldown = 1000;
        damage = 3d;
        velocity = 40f;
        material = Material.WOODEN_HOE;
        maxAmmo = 10;
        ammoName = "9mm";
        rarity = Rarity.RARE;
        minLoot = 1;
        maxLoot = 1;
    }
}