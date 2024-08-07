package cz.jeme.programu.gungaming.config;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

public final class ConfigValue<T> {
    private final @NotNull String name;
    private final @NotNull ArgumentType<T> type;
    private @NotNull T value;

    ConfigValue(final @NotNull String name, final @NotNull ArgumentType<T> type, final @NotNull T defaultValue) {
        if (!name.matches("[a-z0-9_]+"))
            throw new IllegalArgumentException("Name contains invalid characters!");
        this.name = name;
        this.type = type;
        this.value = defaultValue;
    }

    public @NotNull T get() {
        return value;
    }

    public @NotNull String getString(final @NotNull CommandSourceStack source) {
        return switch (value) {
            case final Coordinates coords -> {
                final BlockPos blockPos = coords.getBlockPos(source);
                yield blockPos.getX() + " / " + blockPos.getZ();
            }
            default -> String.valueOf(value);
        };
    }

    public void set(final @NotNull T value) {
        this.value = value;
    }

    public @NotNull ArgumentType<T> type() {
        return type;
    }

    public @NotNull String name() {
        return name;
    }
}
