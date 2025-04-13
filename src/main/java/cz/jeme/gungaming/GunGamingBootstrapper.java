package cz.jeme.gungaming;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("UnstableApiUsage")
@NullMarked
public final class GunGamingBootstrapper implements PluginBootstrap {
    private final Logger logger = LoggerFactory.getLogger(GunGamingBootstrapper.class.getSimpleName());

    @Override
    public void bootstrap(final BootstrapContext context) {
        logger.info("Bootstrap success");
    }

    @Override
    public JavaPlugin createPlugin(final PluginProviderContext context) {
        return GunGaming.instance();
    }
}
