package cz.jeme.programu.gungaming.eventhandlers;

import java.util.Arrays;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import cz.jeme.programu.gungaming.ArrowVelocityListener;
import cz.jeme.programu.gungaming.utils.Materials;
import cz.jeme.programu.gungaming.utils.ScoreboardTagUtils;

public class HitHandler {

	private ArrowVelocityListener arrowVelocityListener;

	public HitHandler(ArrowVelocityListener arrowVelocityListener) {
		this.arrowVelocityListener = arrowVelocityListener;
	}

	public void onProjectileHit(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();

		if (!projectile.getScoreboardTags().contains(ScoreboardTagUtils.HEADER)) {
			return;
		}
		assert projectile instanceof Arrow : "Projectile not Arrow!";

		arrowVelocityListener.removeArrow((Arrow) projectile);

		Map<String, String> tags = ScoreboardTagUtils.getScoreboardTags(projectile);
		boolean isRocket = Boolean.valueOf(tags.get("Rocket"));

		if (isRocket) {
			Location location = projectile.getLocation();
			double x = location.getX();
			double y = location.getY();
			double z = location.getZ();

			World world = projectile.getWorld();

			world.createExplosion(x, y, z, 100f, true, true, projectile);
			projectile.remove();
			return;
		}

		if (event.getHitBlock() == null) {
			projectile.remove();
			return;
		}

		projectile.setGravity(true);

		Block block = event.getHitBlock();
		Material type = block.getType();
		if (Arrays.asList(Materials.GLASSES).contains(type) || Arrays.asList(Materials.GLASS_PANES).contains(type)) {
			block.setType(Material.AIR);
			for (Player player : Bukkit.getOnlinePlayers()) {
				Location particleLocation = block.getLocation().add(0.5, 0.5, 0.5);
				player.spawnParticle(Particle.ITEM_CRACK, particleLocation, 40, 0.2, 0.2, 0.2, 0.1,
						new ItemStack(type));
				player.playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 2);
			}
			projectile.remove();
		}
	}

	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager().getType() != EntityType.ARROW) {
			return;
		}

		Projectile arrow = (Projectile) event.getDamager();

		if (!ScoreboardTagUtils.isCustomTagged(arrow)) {
			return;
		}

		Map<String, String> tags = ScoreboardTagUtils.getScoreboardTags(arrow);
		double damage = Double.valueOf(tags.get("Damage"));

		arrow.remove();

		event.setDamage(damage);
	}
}