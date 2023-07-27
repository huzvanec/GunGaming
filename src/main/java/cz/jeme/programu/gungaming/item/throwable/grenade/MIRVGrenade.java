package cz.jeme.programu.gungaming.item.throwable.grenade;

import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

public class MIRVGrenade extends Grenade {
    @Override
    protected void setup() {
        name = "MIRV Grenade";
        info = "explodes into eight small explosive pieces";
        customModelData = 3;
        rarity = Rarity.EPIC;
        throwCooldown = 2000;
        damage = 25d;
    }

    @Override
    public int getMinLoot() {
        return 1;
    }

    @Override
    public int getMaxLoot() {
        return 3;
    }

    @Override
    public void onThrownHit(ProjectileHitEvent event, Projectile thrown) {
        thrown.getWorld().createExplosion(thrown, thrown.getLocation(), 4f, false, true);
        float horizontalPower = 0.3f;
        float circleMultiplier = 1.570796327f; // pi / 2
        shootSmallGrenade(thrown, horizontalPower, 0f);
        shootSmallGrenade(thrown, horizontalPower / circleMultiplier, horizontalPower / circleMultiplier);
        shootSmallGrenade(thrown, 0f, horizontalPower);
        shootSmallGrenade(thrown, horizontalPower / (-circleMultiplier), horizontalPower / circleMultiplier);
        shootSmallGrenade(thrown, horizontalPower * -1f, 0f);
        shootSmallGrenade(thrown, horizontalPower / (-circleMultiplier), horizontalPower / (-circleMultiplier));
        shootSmallGrenade(thrown, 0f, horizontalPower * -1f);
        shootSmallGrenade(thrown, horizontalPower / circleMultiplier, horizontalPower / (-circleMultiplier));
    }

    private void shootSmallGrenade(Projectile thrown, float x, float z) {
        Snowball smallGrenade = thrown.getWorld().spawn(thrown.getLocation(), Snowball.class);
        Namespaces.THROWN.set(smallGrenade, "Small Grenade");
        Namespaces.THROWN_DAMAGE.set(smallGrenade, SmallGrenade.DAMAGE);
        smallGrenade.setVelocity(new Vector(x, 0.3f, z));
    }
}
