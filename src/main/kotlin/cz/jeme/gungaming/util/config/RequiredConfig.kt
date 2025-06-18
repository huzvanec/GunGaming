package cz.jeme.gungaming.util.config

import net.kyori.adventure.text.format.TextColor
import org.bukkit.configuration.ConfigurationSection
import kotlin.reflect.KClass

private fun <T : Any> ConfigurationSection.require(
    verifier: (String) -> Boolean,
    accessor: (String) -> T?,
    type: KClass<T>,
    path: String
): T {
    require(verifier(path)) {
        "Missing or invalid required value of type '${type.simpleName}' at path: '$currentPath.$path'"
    }
    return accessor(path)!!
}

fun ConfigurationSection.requireInt(path: String): Int = require(
    ::isInt,
    ::getInt,
    Int::class,
    path
)

fun ConfigurationSection.requireLong(path: String): Long = require(
    ::isLong,
    ::getLong,
    Long::class,
    path
)

fun ConfigurationSection.requireDouble(path: String): Double = require(
    ::isDouble,
    ::getDouble,
    Double::class,
    path
)

fun ConfigurationSection.requireString(path: String): String = require(
    ::isString,
    ::getString,
    String::class,
    path
)

fun ConfigurationSection.requireBoolean(path: String): Boolean = require(
    ::isBoolean,
    ::getBoolean,
    Boolean::class,
    path
)

fun ConfigurationSection.requireConfigurationSection(path: String): ConfigurationSection = require(
    ::isConfigurationSection,
    ::getConfigurationSection,
    ConfigurationSection::class,
    path
)

fun ConfigurationSection.requireCSSHexColor(path: String): TextColor = require(
    ::isCSSHexColor,
    ::getCSSHexColor,
    TextColor::class,
    path
)