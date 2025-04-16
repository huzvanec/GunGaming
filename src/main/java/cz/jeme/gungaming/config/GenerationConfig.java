package cz.jeme.gungaming.config;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinate;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

@NullMarked
public final class GenerationConfig {
    private GenerationConfig() {
        throw new AssertionError();
    }

    private static final Map<String, ConfigValue<?>> VALUES = new HashMap<>();

    public static Map<String, ConfigValue<?>> values() {
        return Map.copyOf(VALUES);
    }

    private static <T> ConfigValue<T> value(final String name, final ArgumentType<T> type, final T defaultValue) {
        final ConfigValue<T> value = new ConfigValue<>(name, type, defaultValue);
        VALUES.put(value.name(), value);
        return value;
    }

    public static final ConfigValue<Integer> WIDTH = value(
            "width",
            IntegerArgumentType.integer(50, 10_000),
            500
    );
    public static final ConfigValue<Integer> LENGTH = value(
            "length",
            IntegerArgumentType.integer(50, 10_000),
            500
    );
    public static final ConfigValue<Coordinates> CENTER = value(
            "center",
            Vec2Argument.vec2(),
            new WorldCoordinates(
                    new WorldCoordinate(false, 0),
                    new WorldCoordinate(true, 0),
                    new WorldCoordinate(false, 0)
            )
    );
    public static final ConfigValue<Integer> BPS = value(
            "bps",
            IntegerArgumentType.integer(100, 10_000),
            1000
    );
    public static final ConfigValue<Integer> GUN_AMMO_PERCENTAGE = value(
            "gun_ammo_percentage",
            IntegerArgumentType.integer(0, 100),
            50
    );
}