package cz.jeme.gungaming.item.throwable;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.persistence.PersistentData;
import org.bukkit.entity.Snowball;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ThrownHelper {
    public static final PersistentData<String, String> THROWABLE_KEY_DATA = PersistentData.ofString(GunGaming.key("thrown_throwable_key"));
    public static final PersistentData<Double, Double> MAX_DAMAGE_DATA = PersistentData.ofDouble(GunGaming.key("thrown_max_damage"));

    private ThrownHelper() {
        throw new AssertionError();
    }

    public static boolean isThrown(final Snowball snowball) {
        return THROWABLE_KEY_DATA.check(snowball);
    }

    public static Throwable getThrowable(final Snowball snowball) {
        return Throwable.of(THROWABLE_KEY_DATA.require(snowball));
    }
}
