package cz.jeme.programu.gungaming.items.guns;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.Color;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import cz.jeme.programu.gungaming.items.CustomItem;
import cz.jeme.programu.gungaming.utils.LoreUtils;
import cz.jeme.programu.gungaming.utils.ScoreboardTagUtils;

public abstract class Gun extends CustomItem {

	public Map<String, String> tags = new HashMap<String, String>();
	public Map<String, String> lore = new HashMap<String, String>();

	public Integer shootCooldown = null;

	public Integer reloadCooldown = null;

	public Double damage = null;

	public Float velocity = null;

	public Integer maxAmmo = null;

	public Boolean isRocket = true;

	public String ammoTypeName = null;

	public Gun() {
		setup();

		assert shootCooldown != null : "No shoot cooldown given!";
		assert reloadCooldown != null : "No reload cooldown given!";
		assert damage != null : "No damage given!";
		assert velocity != null : "No bullet speed given!";
		assert maxAmmo != null : "No max ammo given!";
		assert ammoTypeName != null : "No ammo type given!";

		lore.put("Scope", "10");
		lore.put("Ammo", String.valueOf(maxAmmo) + "/" + String.valueOf(maxAmmo));
		LoreUtils.addLore(item, lore);

		tags.put("Damage", String.valueOf(damage));
	}

	public Arrow shoot(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Arrow arrow = player.launchProjectile(Arrow.class, player.getLocation().getDirection().multiply(velocity));
		arrow.setPickupStatus(PickupStatus.DISALLOWED);
		if (!isRocket) {
			arrow.setColor(Color.RED);
			arrow.setGravity(false);
			tags.put("Rocket", "true");
		} else {
			tags.put("Rocket", "false");
		}
		arrow.setBounce(false);
		arrow.setFallDistance(0);
		ScoreboardTagUtils.addScoreboardTags(arrow, tags);
		return arrow;
	}
}
