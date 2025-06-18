package cz.jeme.gungaming.util

import cz.jeme.gungaming.GunGaming
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.KeyPattern
import net.kyori.adventure.key.Keyed
import org.bukkit.NamespacedKey

fun Key.toNamespacedKey() = this as? NamespacedKey ?: NamespacedKey(namespace(), value())

fun String.namespacedWithDefaultOrGunGaming() = namespacedWithDefaultOr(GunGaming.namespace)

fun String.namespacedWithDefaultOr(@KeyPattern.Namespace namespace: String): Key {
    if (!Key.parseable(this))
        throw IllegalArgumentException("Invalid key: '$this'")
    return if (Key.DEFAULT_SEPARATOR in this) Key.key(this)
    else Key.key(namespace, this)
}

fun String.namespacedBy(
    keyed: Keyed
) = namespacedWithDefaultOr(keyed.key().namespace())