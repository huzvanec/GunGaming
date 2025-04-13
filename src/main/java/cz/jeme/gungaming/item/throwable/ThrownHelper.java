package cz.jeme.gungaming.item.throwable;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.persistence.PersistentData;
import org.bukkit.entity.Snowball;
import org.jetbrains.annotations.NotNull;

public final class ThrownHelper {
    public static final @NotNull PersistentData<String, String> THROWABLE_KEY_DATA = PersistentData.ofString(GunGaming.key("thrown_throwable_key"));
    public static final @NotNull PersistentData<Double, Double> MAX_DAMAGE_DATA = PersistentData.ofDouble(GunGaming.key("thrown_max_damage"));

    private ThrownHelper() {
        throw new AssertionError();
    }

    public static boolean isThrown(final @NotNull Snowball snowball) {
        return THROWABLE_KEY_DATA.check(snowball);
    }

    public static @NotNull Throwable getThrowable(final @NotNull Snowball snowball) {
        return Throwable.of(THROWABLE_KEY_DATA.require(snowball));
    }
}
