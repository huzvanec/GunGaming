package cz.jeme.programu.gungaming.command.gg;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.command.GGCommandExceptionType;
import cz.jeme.programu.gungaming.game.GameTeam;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
final class GameTeamArgument implements CustomArgumentType<GameTeam, Key> {
    private static boolean isValidChar(final char c) {
        return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '.' || c == '_' || c == '-' || c == ':';
    }

    @Override
    public @NotNull GameTeam parse(final @NotNull StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        while (reader.canRead() && isValidChar(reader.peek())) reader.skip();
        String keyStr = reader.getString().substring(start, reader.getCursor());
        if (!keyStr.startsWith(GunGaming.NAMESPACE)) keyStr = GunGaming.NAMESPACE + ":" + keyStr;
        if (!GameTeam.exists(keyStr)) {
            reader.setCursor(start);
            throw new GGCommandExceptionType(Component.text("Unknown team '%s'".formatted(keyStr)))
                    .createWithContext(reader);
        }
        return GameTeam.ofKey(keyStr);
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(final @NotNull CommandContext<S> context, final @NotNull SuggestionsBuilder builder) {
        final StringReader reader = new StringReader(builder.getInput());
        reader.setCursor(builder.getStart());
        while (reader.canRead()) reader.skip();
        final String current = reader.getString().substring(builder.getStart(), reader.getCursor());
        GameTeam.cached().stream()
                .map(team -> team.key().asString())
                .filter(keyStr -> keyStr.contains(current.toLowerCase()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}