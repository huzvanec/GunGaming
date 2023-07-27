package cz.jeme.programu.gungaming.item.throwable;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Grenade extends Throwable {
    @Override
    protected void setup() {
        name = "Grenade";
        info = "an explosive throwable weapon";
        customModelData = 1;
        rarity = Rarity.EPIC;
        throwCooldown = 1000;
        damage = 20d;
    }

    @Override
    public int getMinLoot() {
        return 1;
    }

    @Override
    public int getMaxLoot() {
        return 5;
    }

    @Override
    public void onThrownHit(ProjectileHitEvent event, Projectile thrown) {
        thrown.getWorld().createExplosion(thrown, thrown.getLocation(), 3f, false, true);
    }
}
