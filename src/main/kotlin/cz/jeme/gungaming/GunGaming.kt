package cz.jeme.gungaming

import org.bukkit.plugin.java.JavaPlugin

internal object GunGaming : JavaPlugin() {
    val logger = componentLogger

    override fun onEnable() {
        logger.info("Hello World!")
    }
}
