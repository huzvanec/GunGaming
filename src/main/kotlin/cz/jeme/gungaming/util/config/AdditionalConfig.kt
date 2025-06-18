package cz.jeme.gungaming.util.config

import net.kyori.adventure.text.format.TextColor
import org.bukkit.configuration.ConfigurationSection

fun ConfigurationSection.isCSSHexColor(path: String): Boolean {
    return isString(path) && TextColor.fromCSSHexString(getString(path)!!) != null
}

fun ConfigurationSection.getCSSHexColor(path: String): TextColor? {
    return TextColor.fromCSSHexString(getString(path) ?: return null)
}

fun ConfigurationSection.getChildConfigurationSections(deep: Boolean): List<ConfigurationSection> {
    return getKeys(deep).mapNotNull { getConfigurationSection(it) }
}