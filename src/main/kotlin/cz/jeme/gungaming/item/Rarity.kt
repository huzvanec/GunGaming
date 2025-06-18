package cz.jeme.gungaming.item

import cz.jeme.gungaming.element.CustomElement
import cz.jeme.gungaming.util.config.requireCSSHexColor
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.format.TextColor
import org.bukkit.configuration.ConfigurationSection

class Rarity(key: Key, definition: ConfigurationSection) : CustomElement(key, definition) {
    val color: TextColor = definition.requireCSSHexColor("color")
}