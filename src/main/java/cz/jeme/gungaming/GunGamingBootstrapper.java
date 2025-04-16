package cz.jeme.gungaming;

import io.papermc.paper.ServerBuildInfo;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.apache.logging.log4j.LogManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("UnstableApiUsage")
@NullMarked
public final class GunGamingBootstrapper implements PluginBootstrap {
    private static final String REQUIRED_SERVER_VERSION = "1.21.5";

    private final Logger logger = LoggerFactory.getLogger(GunGamingBootstrapper.class.getSimpleName());

    @Override
    public void bootstrap(final BootstrapContext context) {
        final String pluginVersion = context.getPluginMeta().getVersion();

        final String serverVersion = ServerBuildInfo.buildInfo().minecraftVersionId();
        if (!REQUIRED_SERVER_VERSION.equals(serverVersion)) {
            logger.error("""
                            \nGunGaming v{} is not compatible with your current Minecraft version.
                            Present server version: {}
                            Required server version: {}
                            Please download the correct PaperMC version from: https://papermc.io/downloads/paper
                            """,
                    pluginVersion, serverVersion, REQUIRED_SERVER_VERSION);
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
