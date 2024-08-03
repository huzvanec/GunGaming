package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.data.Data;
import org.bukkit.entity.AbstractArrow;
import org.jetbrains.annotations.NotNull;

public final class BulletHelper {
    public static final @NotNull Data<String, String> GUN_KEY_DATA = Data.ofString(GunGaming.namespaced("bullet_gun_key"));
    public static final @NotNull Data<Double, Double> DAMAGE_DATA = Data.ofDouble(GunGaming.namespaced("bullet_damage"));
    // TODO knockback?

    private BulletHelper() {
        throw new AssertionError();
    }

    public static boolean isBullet(final @NotNull AbstractArrow arrow) {
        return GUN_KEY_DATA.check(arrow);
    }

    public static @NotNull Gun getGun(final @NotNull AbstractArrow arrow) {
        return Gun.of(GUN_KEY_DATA.require(arrow));
    }
}
