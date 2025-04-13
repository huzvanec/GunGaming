package cz.jeme.gungaming.item.gun;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.persistence.PersistentData;
import org.bukkit.entity.AbstractArrow;
import org.jetbrains.annotations.NotNull;

public final class BulletHelper {
    public static final @NotNull PersistentData<String, String> GUN_KEY_DATA = PersistentData.ofString(GunGaming.key("bullet_gun_key"));
    public static final @NotNull PersistentData<Double, Double> DAMAGE_DATA = PersistentData.ofDouble(GunGaming.key("bullet_damage"));
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
