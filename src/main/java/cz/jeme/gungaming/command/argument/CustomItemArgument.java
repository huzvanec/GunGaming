package cz.jeme.gungaming.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import cz.jeme.gungaming.ElementManager;
import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.command.GGCommandExceptionType;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.loot.Rarity;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
@ApiStatus.Internal
public final class CustomItemArgument implements CustomArgumentType<CustomItem, Key> {
    @Override
    public CustomItem parse(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        while (reader.canRead() && reader.peek() != ' ') reader.skip();
        String keyStr = reader.getString().substring(start, reader.getCursor());
        if (!keyStr.startsWith(GunGaming.NAMESPACE)) keyStr = GunGaming.NAMESPACE + ":" + keyStr;
        if (!CustomItem.is(keyStr)) {
            reader.setCursor(start);
            throw new GGCommandExceptionType(Component.text("Unknown item '%s'".formatted(keyStr)))
                    .createWithContext(reader);
        }
        return CustomItem.of(keyStr);
    }

    @Override
    public ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        final String tag = StringArgumentType.getString(context.getLastChild(), "tag");
        final StringReader reader = new StringReader(builder.getInput());
        reader.setCursor(builder.getStart());
        while (reader.canRead()) reader.skip();
        final String current = reader.getString().substring(builder.getStart(), reader.getCursor());
        ElementManager.INSTANCE.getItems(tag).stream()
                .filter(item -> item.rarity() != Rarity.UNOBTAINABLE)
                .map(item -> item.key().asString())
                .filter(keyStr -> keyStr.contains(current.toLowerCase()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}