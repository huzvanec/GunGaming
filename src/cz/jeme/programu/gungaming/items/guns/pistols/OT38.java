package cz.jeme.programu.gungaming.items.guns.pistols;

import org.bukkit.Material;

import cz.jeme.programu.gungaming.items.guns.Gun;

public class OT38 extends Gun {

	@Override
	protected void setup() {
		name = "OT-38";
		loreLine = "Common pistol";
		shootCooldown = 600;
		reloadCooldown = 1500;
		damage = 8d;
		velocity = 40f;
		material = Material.STONE_HOE;
		maxAmmo = 5;
		isRocket = true;
		ammoTypeName = "7.62mm";
	}
}