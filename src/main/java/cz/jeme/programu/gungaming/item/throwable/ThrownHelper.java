package cz.jeme.programu.gungaming.item.throwable;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.data.Data;
import org.bukkit.entity.Snowball;
import org.jetbrains.annotations.NotNull;

public final class ThrownHelper {
    public static final @NotNull Data<String, String> THROWABLE_KEY_DATA = Data.ofString(GunGaming.namespaced("thrown_throwable_key"));
    public static final @NotNull Data<Double, Double> MAX_DAMAGE_DATA = Data.ofDouble(GunGaming.namespaced("thrown_max_damage"));

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
