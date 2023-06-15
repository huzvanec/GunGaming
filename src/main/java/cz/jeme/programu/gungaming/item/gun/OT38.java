package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class OT38 extends Gun {

    @Override
    protected void setup() {
        name = "OT-38";
        info = "A lousy revolver";
        shootCooldown = 600;
        reloadCooldown = 1500;
        damage = 4.5d;
        velocity = 40f;
        material = Material.STONE_HOE;
        maxAmmo = 5;
        ammoName = "7.62mm";
        rarity = Rarity.RARE;
        minLoot = 1;
        maxLoot = 1;
    }
}