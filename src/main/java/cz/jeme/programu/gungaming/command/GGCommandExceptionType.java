package cz.jeme.programu.gungaming.command;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
class GGCommandExceptionType implements CommandExceptionType {
    protected final @NotNull Message message;

    public GGCommandExceptionType(final @NotNull Message message) {
        this.message = message;
    }

    public GGCommandExceptionType(final @NotNull Component message) {
        this(MessageComponentSerializer.message().serialize(message));
    }

    public @NotNull CommandSyntaxException create() {
        return new CommandSyntaxException(this, this.message);
    }

    public @NotNull CommandSyntaxException createWithContext(final @NotNull ImmutableStringReader reader) {
        return new CommandSyntaxException(this, this.message, reader.getString(), reader.getCursor());
    }
}