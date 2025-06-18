package cz.jeme.gungaming

import cz.jeme.gungaming.element.ElementRegistry
import net.kyori.adventure.key.KeyPattern
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileOutputStream

internal object GunGaming : JavaPlugin() {
    @KeyPattern.Namespace
    val namespace = name.lowercase()

    val definitionPacksFolder = File(dataFolder, "packs").apply {
        if (!exists() && !mkdirs()) throw IllegalStateException("Could not create definitions folder")
    }
    val baseDefinitionPackYaml = File(definitionPacksFolder, "gungaming.yml")

    override fun onEnable() {
        // TODO don't overwrite
//        if (!baseDefinitionsYaml.exists()) {
        getResource("definitions.yml")!!.use { inputStream ->
            FileOutputStream(baseDefinitionPackYaml).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
//        }

        ElementRegistry.loadDefinitionPacks()
    }

    fun key(@KeyPattern.Value key: String) = NamespacedKey(namespace, key)
}