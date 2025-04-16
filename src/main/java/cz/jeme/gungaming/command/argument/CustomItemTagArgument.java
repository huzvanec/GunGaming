package cz.jeme.gungaming.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import cz.jeme.gungaming.ElementManager;
import cz.jeme.gungaming.command.GGCommandExceptionType;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
@ApiStatus.Internal
public final class CustomItemTagArgument implements CustomArgumentType<String, String> {
    @Override
    public String parse(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        while (reader.canRead() && reader.peek() != ' ') reader.skip();
        final String tag = reader.getString().substring(start, reader.getCursor());
        if (!ElementManager.INSTANCE.existsTag(tag)) {
            reader.setCursor(start);
            throw new GGCommandExceptionType(Component.text("Unknown item tag '%s'".formatted(tag)))
                    .createWithContext(reader);
        }
        return tag;
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
        ElementManager.INSTANCE.tags().stream()
                .filter(tag -> tag.contains(current.toLowerCase()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
