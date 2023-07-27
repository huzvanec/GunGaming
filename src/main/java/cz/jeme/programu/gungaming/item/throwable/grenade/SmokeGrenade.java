package cz.jeme.programu.gungaming.item.throwable.grenade;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;

public class SmokeGrenade extends Grenade {
    @Override
    protected void setup() {
        name = "Smoke Grenade";
        info = "throwable weapon that blinds enemies";
        customModelData = 2;
        rarity = Rarity.UNCOMMON;
        throwCooldown = 1000;
        damage = 0d;
    }

    @Override
    public int getMinLoot() {
        return 1;
    }

    @Override
    public int getMaxLoot() {
        return 6;
    }

    @Override
    public void onThrownHit(ProjectileHitEvent event, Projectile thrown) {
//        thrown.getWorld().createExplosion(thrown, thrown.getLocation(), 3f, false, true);
    }
}
