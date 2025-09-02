package cz.jeme.gungaming.command;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record GGCommandExceptionType(Message message) implements CommandExceptionType {

    public GGCommandExceptionType(final Component message) {
        this(MessageComponentSerializer.message().serialize(message));
    }

    public CommandSyntaxException create() {
        return new CommandSyntaxException(this, this.message);
    }

    public CommandSyntaxException createWithContext(final ImmutableStringReader reader) {
        return new CommandSyntaxException(this, this.message, reader.getString(), reader.getCursor());
    }
}