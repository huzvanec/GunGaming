package cz.jeme.gungaming.item

import cz.jeme.gungaming.GunGaming
import cz.jeme.gungaming.element.CustomElement
import cz.jeme.gungaming.element.ElementRegistry
import cz.jeme.gungaming.item.behavior.ItemBehavior
import cz.jeme.gungaming.util.config.requireString
import cz.jeme.gungaming.util.namespacedBy
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.configuration.ConfigurationSection

class CustomItem(key: Key, def: ConfigurationSection) : CustomElement(key, def) {
    val rarity: Rarity
    val description: Component? = def.getRichMessage("description")

    // behavior name -> behavior
    private val _behaviors = mutableMapOf<String, ItemBehavior>()
    val behaviors: Map<String, ItemBehavior> = _behaviors

    init {
        val rarityKey = def.requireString("rarity").namespacedBy(this)
        rarity = ElementRegistry.rarities[rarityKey]
            ?: throw IllegalStateException("Rarity '$rarityKey' used in '${key.asString()}' does not exist")

        def.getConfigurationSection("behaviors")?.let { section ->
            // banned behavior class -> name of the behavior it conflicts with
            val bannedBehaviors = mutableMapOf<Class<out ItemBehavior>, String>()
            section.getKeys(false).forEach { behaviorName ->
                val clazz = ItemBehavior.registry[behaviorName]
                    ?: throw IllegalStateException("Item behavior '$behaviorName' used in '${key.asString()}' does not exist")

                val behavior = ItemBehavior.construct(
                    clazz,
                    this,
                    section.getConfigurationSection(behaviorName)
                )

                // the name of the behavior that banned this behavior or null
                val bannedByBehavior = bannedBehaviors[
                    bannedBehaviors.keys.firstOrNull { clazz ->
                        clazz.isInstance(behavior)
                    }
                ]

                // the name of the already loaded behavior that this behavior conflicts with or null
                val conflictingBehavior = behavior.conflicts.firstOrNull { clazz ->
                    behaviors.any { clazz.java.isInstance(it) }
                }?.java
                (bannedByBehavior ?: conflictingBehavior)?.let { conflict ->
                    GunGaming.componentLogger.warn(
                        "Item behavior '$behaviorName' used in '${key.asString()}' conflicts with '${bannedBehaviors[conflict]}' and will be ignored."
                    )
                }

                behavior.conflicts.forEach { bannedBehaviors.putIfAbsent(it.java, behaviorName) }

                _behaviors[behaviorName] = behavior
            }
        }
    }
}