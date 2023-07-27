package cz.jeme.programu.gungaming.item.throwable.grenade;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;

public class SmallGrenade extends Grenade {
    public static final double DAMAGE = 10d;
    @Override
    protected void setup() {
        name = "Small Grenade";
        info = "a grenade used for the small MIRV explosion";
        customModelData = 0;
        rarity = Rarity.UNOBTAINABLE;
        throwCooldown = 0;
        damage = DAMAGE;
    }

    @Override
    public int getMinLoot() {
        return 0;
    }

    @Override
    public int getMaxLoot() {
        return 0;
    }

    @Override
    public void onThrownHit(ProjectileHitEvent event, Projectile thrown) {
        thrown.getWorld().createExplosion(thrown, thrown.getLocation(), 2f, false, true);
    }
}
