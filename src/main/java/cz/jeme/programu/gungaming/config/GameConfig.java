package cz.jeme.programu.gungaming.config;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinate;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class GameConfig {
    private GameConfig() {
        throw new AssertionError();
    }

    private static final @NotNull Map<String, ConfigValue<?>> VALUES = new HashMap<>();

    public static @NotNull Map<String, ConfigValue<?>> values() {
        return Map.copyOf(VALUES);
    }

    private static <T> @NotNull ConfigValue<T> value(final @NotNull String name, final @NotNull ArgumentType<T> type, final @NotNull T defaultValue) {
        final ConfigValue<T> value = new ConfigValue<>(name, type, defaultValue);
        VALUES.put(value.name(), value);
        return value;
    }

    public static final @NotNull ConfigValue<Integer> SIZE = value(
            "size",
            IntegerArgumentType.integer(50, 10_000),
            500
    );
    public static final @NotNull ConfigValue<Integer> TEAM_PLAYERS = value(
            "team_players",
            IntegerArgumentType.integer(1, 10),
            1
    );
    public static final @NotNull ConfigValue<Coordinates> CENTER = value(
            "center",
            Vec2Argument.vec2(),
            new WorldCoordinates(
                    new WorldCoordinate(false, 0),
                    new WorldCoordinate(true, 0),
                    new WorldCoordinate(false, 0)
            )
    );
    public static final @NotNull ConfigValue<Coordinates> LOBBY_SPAWN = value(
            "lobby_spawn",
            Vec2Argument.vec2(),
            new WorldCoordinates(
                    new WorldCoordinate(false, 0),
                    new WorldCoordinate(true, 0),
                    new WorldCoordinate(false, 0)
            )
    );
    public static final @NotNull ConfigValue<Integer> GAME_SECONDS = value(
            "game_seconds",
            IntegerArgumentType.integer(60, 5 * 60 * 60),
            25 * 60 + 15
    );
    public static final @NotNull ConfigValue<Integer> COUNTDOWN_SECONDS = value(
            "countdown_seconds",
            IntegerArgumentType.integer(3, 10 * 60),
            10
    );
    public static final @NotNull ConfigValue<Integer> GRACE_PERIOD_SECONDS = value(
            "grace_period_seconds",
            IntegerArgumentType.integer(60, 60 * 60),
            3 * 60 + 30
    );
    public static final @NotNull ConfigValue<Integer> RESPAWN_SECONDS = value(
            "respawn_seconds",
            IntegerArgumentType.integer(3, 60 * 60),
            30
    );
    public static final @NotNull ConfigValue<Integer> REFILL_SECONDS = value(
            "refill_seconds",
            IntegerArgumentType.integer(60, 60 * 60),
            9 * 60
    );
    public static final @NotNull ConfigValue<Integer> AIR_DROP_MIN_SECONDS = value(
            "air_drop_min_seconds",
            IntegerArgumentType.integer(30, 60 * 60),
            5 * 60
    );
    public static final @NotNull ConfigValue<Integer> AIR_DROP_MAX_SECONDS = value(
            "air_drop_max_seconds",
            IntegerArgumentType.integer(30, 60 * 60),
            12 * 60
    );
    public static final @NotNull ConfigValue<Integer> WORLD_TIME = value(
            "world_time",
            IntegerArgumentType.integer(0),
            1000
    );
    public static final @NotNull ConfigValue<Integer> TEAM_ANNOUNCE_SECONDS = value(
            "team_announce_seconds",
            IntegerArgumentType.integer(3, 60 * 60),
            10
    );
    public static final @NotNull ConfigValue<Integer> DEATH_DROP_PERCENTAGE = value(
            "death_drop_percentage",
            IntegerArgumentType.integer(0, 100),
            50
    );
    public static final @NotNull ConfigValue<Integer> RESPAWN_PROTECTION_SECONDS = value(
            "respawn_protection_seconds",
            IntegerArgumentType.integer(0, 10 * 60),
            15
    );
    public static final @NotNull ConfigValue<Boolean> SHOW_PLAYER_NAMETAGS = value(
            "show_player_nametags",
            BoolArgumentType.bool(),
            false
    );
}
