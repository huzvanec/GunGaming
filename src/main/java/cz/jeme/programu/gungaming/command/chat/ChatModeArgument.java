package cz.jeme.programu.gungaming.command.chat;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import cz.jeme.programu.gungaming.command.GGCommandExceptionType;
import cz.jeme.programu.gungaming.game.Game;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
final class ChatModeArgument implements CustomArgumentType<Game.ChatMode, String> {
    @Override
    public @NotNull Game.ChatMode parse(final @NotNull StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        while (reader.canRead()) reader.skip();
        final String name = reader.getString().substring(start, reader.getCursor());
        try {
            return Game.ChatMode.valueOf(name.toUpperCase());
        } catch (final IllegalArgumentException e) {
            reader.setCursor(start);
            throw new GGCommandExceptionType(Component.text("Unknown chat mode '%s'".formatted(name)))
                    .createWithContext(reader);
        }
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
        Game.ChatMode.cached().stream()
                .map(Game.ChatMode::toString)
                .filter(name -> name.contains(current.toLowerCase()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}