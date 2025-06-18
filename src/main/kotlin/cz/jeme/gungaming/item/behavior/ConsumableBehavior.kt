package cz.jeme.gungaming.item.behavior

import cz.jeme.gungaming.item.CustomItem
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Consumable
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KClass

@Suppress("UnstableApiUsage")
class ConsumableBehavior(item: CustomItem, definition: ConfigurationSection?) : ItemBehavior(item, definition) {
    override fun setup(stack: ItemStack) {
        stack.setData(
            DataComponentTypes.CONSUMABLE,
            Consumable.consumable()
        )
    }

    override val conflicts = setOf<KClass<out ItemBehavior>>(
        BomboclattBehavior::class
    )
}