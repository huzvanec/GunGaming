package cz.jeme.gungaming.item.behavior

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import cz.jeme.gungaming.GunGaming
import cz.jeme.gungaming.item.CustomItem
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KClass

@Suppress("NOTHING_TO_INLINE")
private inline infix fun <T : ItemBehavior> String.constructed(
    noinline ctor: (CustomItem, ConfigurationSection?) -> T
) = this to ItemBehavior.Factory(ctor)

abstract class ItemBehavior protected constructor(
    val item: CustomItem,
    definition: ConfigurationSection?
) : Listener {
    companion object {
        val registry: BiMap<String, KClass<out ItemBehavior>> =
            HashBiMap.create<String, KClass<out ItemBehavior>>().apply {
                listOf(
                    "consumable" to ConsumableBehavior::class,
                    "bomboclatt" to BomboclattBehavior::class
                ).forEach { (name, clazz) -> this[name] = clazz }
            }

        fun <T : ItemBehavior> construct(
            clazz: KClass<T>,
            item: CustomItem,
            definition: ConfigurationSection?
        ): T {
            return clazz.java.getConstructor(CustomItem::class.java, ConfigurationSection::class.java)
                .newInstance(
                    item,
                    definition
                )
        }

        @Suppress("UNCHECKED_CAST")
        fun construct(
            name: String,
            item: CustomItem,
            definition: ConfigurationSection?
        ): ItemBehavior? {
            return construct(
                registry[name] ?: return null,
                item,
                definition
            )
        }
    }

    val name: String = registry.inverse()[this::class]!!

    fun interface Factory<T : ItemBehavior> {
        fun construct(item: CustomItem, definition: ConfigurationSection?): T
    }

    init {
        Bukkit.getPluginManager().registerEvents(this, GunGaming)
    }

    open fun setup(stack: ItemStack) {}
    open val conflicts = emptySet<KClass<out ItemBehavior>>()
}