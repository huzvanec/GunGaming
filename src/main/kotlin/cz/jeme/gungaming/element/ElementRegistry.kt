package cz.jeme.gungaming.element

import cz.jeme.gungaming.GunGaming
import cz.jeme.gungaming.item.CustomItem
import cz.jeme.gungaming.item.Rarity
import net.kyori.adventure.key.Key
import org.bukkit.configuration.ConfigurationSection

object ElementRegistry {
    private val _definitionPacks = hashMapOf<String, DefinitionPack>()
    val definitionPacks: Map<String, DefinitionPack> = _definitionPacks

    private val _elements = hashMapOf<Key, CustomElement>()
    val elements: Map<Key, CustomElement> = _elements

    private val _items = hashMapOf<Key, CustomItem>()
    val items: Map<Key, CustomItem> = _items

    private val _rarities = hashMapOf<Key, Rarity>()
    val rarities: Map<Key, Rarity> = _rarities

    private fun <T : CustomElement> registerElement(
        pack: DefinitionPack,
        definition: ConfigurationSection,
        factory: CustomElement.Factory<T>,
        vararg registries: MutableMap<Key, in T>
    ) {
        val key = Key.key(pack.namespace, definition.name)
        val element = factory.construct(key, definition)
        if (_elements.containsKey(element.key))
            throw IllegalStateException("Duplicate element key: '${definition.currentPath}.$key'")
        _elements[element.key] = element
        registries.forEach { it[element.key] = element }
    }

    private fun <T : CustomElement> registerElements(
        pack: DefinitionPack,
        definitions: List<ConfigurationSection>,
        factory: CustomElement.Factory<T>,
        vararg registries: MutableMap<Key, in T>
    ) = definitions.forEach { registerElement(pack, it, factory, *registries) }

    fun loadDefinitionPacks(log: Boolean = true) {
        val start = System.currentTimeMillis()
        if (log) GunGaming.componentLogger.info("Loading definition packs...")

        GunGaming.definitionPacksFolder.listFiles()!!.forEach { file ->
            val pack = DefinitionPack(file)
            _definitionPacks[pack.namespace] = pack
        }

        val registered = hashSetOf<String>()
        val registering = mutableListOf<String>()
        val stack = ArrayDeque<DefinitionPack>()

        for (pack in _definitionPacks.values) {
            if (pack.namespace in registered) continue
            stack += pack

            while (stack.isNotEmpty()) {
                val current = stack.removeFirst()
                registering += current.namespace
                if (current.dependencies.all { it in registered }) { // all dependencies ok, register
                    registering -= current.namespace
                    registered += current.namespace
                    registerDefinition(current)
                } else {
                    stack.addFirst(pack) // add back, can't be registered yet, missing dependencies
                    current.dependencies
                        .filter { it !in registered } // don't register already registered dependencies
                        .asReversed() // reverse to register dependencies in the order defined in the YAML
                        .forEach {
                            if (it in registering) { // cyclic dependency
                                val cycle = (registering.subList(
                                    registering.indexOf(it),
                                    registering.size
                                ) + it).joinToString(" -> ")
                                throw IllegalStateException("Cyclic dependency detected: $cycle")
                            }
                            stack.addFirst(
                                _definitionPacks[it]
                                    ?: throw IllegalStateException("Dependency '$it' of '${current.namespace}' does not exist")
                            )
                        }
                }
            }
        }

        if (log) {
            GunGaming.componentLogger.info(
                "Successfully loaded ${_definitionPacks.size} definition pack(s) (took ${System.currentTimeMillis() - start} ms)"
            )
        }
    }

    private fun registerDefinition(pack: DefinitionPack, log: Boolean = true) {
        if (log) GunGaming.componentLogger.info("Registering definition pack: ${pack.namespace}")

        registerElements(pack, pack.rarities, ::Rarity, _rarities)
        registerElements(pack, pack.items, ::CustomItem, _items)
    }
}