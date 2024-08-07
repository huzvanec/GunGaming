package cz.jeme.programu.gungaming.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import cz.jeme.programu.gungaming.config.ConfigValue;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
final class ConfigValueArgument implements CustomArgumentType<ConfigValue<?>, String> {
    private final @NotNull Map<String, ConfigValue<?>> values;

    public ConfigValueArgument(final @NotNull Map<String, ConfigValue<?>> values) {
        this.values = values;
    }

    private static boolean isValidChar(final char c) {
        return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '_';
    }

    @Override
    public @NotNull ConfigValue<?> parse(final @NotNull StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        while (reader.canRead() && isValidChar(reader.peek())) reader.skip();
        final String name = reader.getString().substring(start, reader.getCursor());
        final ConfigValue<?> value = values.get(name);
        if (value == null) {
            reader.setCursor(start);
            throw new GGCommandExceptionType(Component.text("Unknown value '%s'".formatted(name)))
                    .createWithContext(reader);
        }
        return value;
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull final CommandContext<S> context, @NotNull final SuggestionsBuilder builder) {
        final StringReader reader = new StringReader(builder.getInput());
        reader.setCursor(builder.getStart());
        while (reader.canRead()) reader.skip();
        final String current = reader.getString().substring(builder.getStart(), reader.getCursor());
        values.keySet().stream()
                .filter(tag -> tag.contains(current.toLowerCase()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
