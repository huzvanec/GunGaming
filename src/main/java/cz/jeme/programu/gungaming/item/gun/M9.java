package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class M9 extends Gun {

    @Override
    protected void setup() {
        name = "M9";
        info = "Basic pistol";
        shootCooldown = 400;
        reloadCooldown = 1000;
        damage = 4d;
        velocity = 40f;
        material = Material.WOODEN_HOE;
        maxAmmo = 15;
        ammoName = "9mm";
        rarity = Rarity.RARE;
        minLoot = 1;
        maxLoot = 1;
    }
}