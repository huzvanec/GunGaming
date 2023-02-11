package cz.jeme.programu.gungaming.items.ammo;

import org.bukkit.Material;

public class NineMM extends Ammo {

	@Override
	protected void setup() {
		material = Material.BROWN_DYE;
		name = "9mm";
		loreLine = "Basic ammo for most guns";
	}

}
