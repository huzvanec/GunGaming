package cz.jeme.gungaming;

import io.papermc.paper.ServerBuildInfo;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.apache.logging.log4j.LogManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;


@SuppressWarnings("UnstableApiUsage")
@NullMarked
public final class GunGamingBootstrapper implements PluginBootstrap {
    private final ComponentLogger logger = ComponentLogger.logger(GunGamingBootstrapper.class.getSimpleName());

    @Override
    public void bootstrap(final BootstrapContext context) {
        final String pluginVersion = context.getPluginMeta().getVersion();

        final String requiredServerVersion = context.getPluginMeta().getAPIVersion();

        final String serverVersion = ServerBuildInfo.buildInfo().minecraftVersionId();
        if (!serverVersion.equals(requiredServerVersion)) {
            logger.error("""
                            \nGunGaming v{} is not compatible with your current Minecraft version.
                            Present server version: {}
                            Required server version: {}
                            Please download the correct PaperMC version from: https://papermc.io/downloads/paper
                            """,
                    pluginVersion, serverVersion, requiredServerVersion);
            LogManager.shutdown(); // flush log messages
            Runtime.getRuntime().halt(-1); // terminate
        }
        logger.info("Bootstrap success");
    }

    @Override
    public JavaPlugin createPlugin(final PluginProviderContext context) {
        return GunGaming.instance();
    }
}
