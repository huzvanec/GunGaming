package cz.jeme.programu.gungaming.util.item;

import cz.jeme.programu.gungaming.util.Namespaces;
import org.bukkit.entity.Projectile;

public final class Bullets {
    private Bullets() {
    }

    public static boolean isBullet(Projectile projectile) {
        return Namespaces.BULLET.has(projectile);
    }
}
