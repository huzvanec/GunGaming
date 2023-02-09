package cz.jeme.programu.gungaming.items.guns.pistols;

import org.bukkit.Material;

import cz.jeme.programu.gungaming.items.guns.Gun;

public class M9 extends Gun {

	@Override
	protected void setup() {
		name = "M9";
		loreLine = "Basic pistol";
		shootCooldown = 400;
		reloadCooldown = 1000;
		damage = 4d;
		velocity = 40f;
		material = Material.WOODEN_HOE;
		maxAmmo = 15;
		isRocket = true;
		ammoTypeName = "9mm";
	}
}