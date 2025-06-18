package cz.jeme.gungaming.element

import cz.jeme.gungaming.util.config.getChildConfigurationSections
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.KeyPattern
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

private val yamlExtensions = setOf("yml", "yaml")

class DefinitionPack(val file: File) {
    companion object {
        val base: DefinitionPack by lazy {
            ElementRegistry.definitionPacks["gungaming"]
                ?: throw IllegalStateException("GunGaming definition pack not loaded yet")
        }
    }

    @KeyPattern.Namespace
    val namespace: String
    val yaml: YamlConfiguration
    val dependencies: List<String> by lazy {
        yaml.getStringList("dependencies")
    }
    val rarities: List<ConfigurationSection> by lazy {
        yaml.getConfigurationSection("rarities")?.getChildConfigurationSections(false) ?: emptyList()
    }
    val items: List<ConfigurationSection> by lazy {
        yaml.getConfigurationSection("items")?.getChildConfigurationSections(false) ?: emptyList()
    }

    init {
        require(file.exists()) {
            "Definition file '${file.path}' does not exist"
        }
        require(file.extension in yamlExtensions) {
            "Definition file '${file.path}' is not a YAML file"
        }

        val nameWithoutExt = file.nameWithoutExtension
        require(Key.parseableNamespace(nameWithoutExt)) {
            "Definition file '${file.path}' has an invalid name. Allowed characters are [a-z0-9_.-]."
        }

        namespace = nameWithoutExt

        yaml = YamlConfiguration().apply {
            try {
                load(file)
            } catch (e: FileNotFoundException) {
                throw IllegalArgumentException("Definition file '${file.path}' could not be opened", e)
            } catch (e: IOException) {
                throw IllegalArgumentException("Definition file '${file.path}' could not be read", e)
            } catch (e: InvalidConfigurationException) {
                throw IllegalArgumentException("Definition file '${file.path}' is not a valid YAML file", e)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DefinitionPack) return false

        if (namespace != other.namespace) return false

        return true
    }

    override fun hashCode(): Int {
        return namespace.hashCode()
    }
}