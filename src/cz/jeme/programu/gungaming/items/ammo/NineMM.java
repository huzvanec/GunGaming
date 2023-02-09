package cz.jeme.programu.gungaming.items.ammo;

import org.bukkit.Material;

public class NineMM extends Ammo {

	@Override
	protected void setup() {
		material = Material.GOLD_NUGGET;
		name = "9mm";
		loreLine = "Basic ammo for most guns";
	}

}
