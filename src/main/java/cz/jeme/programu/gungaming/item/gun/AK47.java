package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class AK47 extends Gun {

    @Override
    protected void setup() {
        name = "AK-47";
        info = "pretty good assault rifle";
        shootCooldown = 200;
        reloadCooldown = 2500;
        damage = 2d;
        velocity = 40f;
        material = Material.IRON_HOE;
        maxAmmo = 32;
        ammoName = "7.62mm";
        rarity = Rarity.EPIC;
        minLoot = 1;
        maxLoot = 1;
    }
}