package cz.jeme.programu.gungaming.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import cz.jeme.programu.gungaming.ElementManager;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public final class CustomItemTagCommandArgument implements CustomArgumentType<String, String> {
    private static boolean isValidChar(final char c) {
        return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '.' || c == '_' || c == '-' || c == ':';
    }

    @Override
    public @NotNull String parse(final @NotNull StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        while (reader.canRead() && isValidChar(reader.peek())) reader.skip();
        final String tag = reader.getString().substring(start, reader.getCursor());
        if (!ElementManager.INSTANCE.existsTag(tag)) {
            reader.setCursor(start);
            throw new GGCommandExceptionType(Component.text("Unknown item tag '%s'".formatted(tag)))
                    .createWithContext(reader);
        }
        return tag;
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(final @NotNull CommandContext<S> context, final @NotNull SuggestionsBuilder builder) {
        final StringReader reader = new StringReader(builder.getInput());
        reader.setCursor(builder.getStart());
        while (reader.canRead()) reader.skip();
        final String current = reader.getString().substring(builder.getStart(), reader.getCursor());
        ElementManager.INSTANCE.tags().stream()
                .filter(tag -> tag.contains(current.toLowerCase()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
