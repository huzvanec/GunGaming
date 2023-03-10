package cz.jeme.programu.gungaming.items.guns;

import org.bukkit.Material;

public class RocketLauncher extends Gun {

	@Override
	protected void setup() {
		name = "Rocket Launcher";
		loreLine = "Awesome launchers";
		shootCooldown = 5000;
		reloadCooldown = 10000;
		damage = 15d;
		velocity = 1.5f;
//		velocity = 0.1f;
		material = Material.DIAMOND_HOE;
		maxAmmo = 999;
		isRocket = true;
		ammoName = "Rocket";
	}
}