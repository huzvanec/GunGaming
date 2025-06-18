package cz.jeme.gungaming.command.argument

import com.mojang.brigadier.StringReader
import cz.jeme.gungaming.command.ComponentCommandExceptionType
import cz.jeme.gungaming.element.ElementRegistry
import cz.jeme.gungaming.item.CustomItem
import cz.jeme.gungaming.util.namespacedWithDefaultOrGunGaming
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component

@Suppress("UnstableApiUsage")
class CustomItemArgumentType : CustomArgumentType<CustomItem, Key> {
    override fun parse(reader: StringReader): CustomItem {
        val start = reader.cursor
        while (reader.canRead() && reader.peek() != ' ') reader.skip()
        val str = reader.string.substring(start, reader.cursor)

        return (if (Key.parseable(str))
            str.namespacedWithDefaultOrGunGaming().let { ElementRegistry.items[it] }
        else null) ?: throw ComponentCommandExceptionType(
            Component.text("Unknown custom item '$str'")
        ).createWithContext(reader)
    }

    override fun getNativeType() = ArgumentTypes.key()
}