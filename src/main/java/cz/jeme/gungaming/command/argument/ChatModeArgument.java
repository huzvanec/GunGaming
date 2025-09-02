package cz.jeme.gungaming.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import cz.jeme.gungaming.command.GGCommandExceptionType;
import cz.jeme.gungaming.game.Game;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
@ApiStatus.Internal
public final class ChatModeArgument implements CustomArgumentType<Game.ChatMode, String> {
    @Override
    public Game.ChatMode parse(final StringReader reader) throws CommandSyntaxException {
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
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
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