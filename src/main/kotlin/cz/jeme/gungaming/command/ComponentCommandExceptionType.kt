package cz.jeme.gungaming.command

import com.mojang.brigadier.ImmutableStringReader
import com.mojang.brigadier.Message
import com.mojang.brigadier.exceptions.CommandExceptionType
import com.mojang.brigadier.exceptions.CommandSyntaxException
import io.papermc.paper.command.brigadier.MessageComponentSerializer
import net.kyori.adventure.text.Component

@Suppress("UnstableApiUsage")
class ComponentCommandExceptionType(val component: Component) : CommandExceptionType {
    val message: Message = MessageComponentSerializer.message().serialize(component)

    fun create() = CommandSyntaxException(
        this,
        message
    )

    fun createWithContext(context: ImmutableStringReader) = CommandSyntaxException(
        this,
        message,
        context.string,
        context.cursor
    )
}