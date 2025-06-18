package cz.jeme.gungaming.element

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import org.bukkit.configuration.ConfigurationSection

abstract class CustomElement protected constructor(
    val key: Key,
    definition: ConfigurationSection
) : Keyed {
    val name: Component = if (definition.isString("name")) miniMessage().deserialize(definition.getString("name")!!)
    else Component.text("<${key.asString()}>", NamedTextColor.RED)

    final override fun key() = key

    fun interface Factory<T : CustomElement> {
        fun construct(key: Key, definition: ConfigurationSection): T
    }
}