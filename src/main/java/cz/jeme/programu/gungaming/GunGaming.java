package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.command.GGCommand;
import cz.jeme.programu.gungaming.game.Game;
import cz.jeme.programu.gungaming.game.lobby.Lobby;
import cz.jeme.programu.gungaming.item.attachment.ZoomManager;
import cz.jeme.programu.gungaming.item.gun.ReloadManager;
import cz.jeme.programu.gungaming.loot.crate.CrateGenerator;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.logging.Logger;


public final class GunGaming extends JavaPlugin {
    private static @Nullable GunGaming instance;
    public static final @NotNull String NAMESPACE = "gungaming";
    private static @Nullable Logger logger;

    @Override
    public void onEnable() {
        final long start = System.currentTimeMillis();
        if (instance != null)
            throw new IllegalStateException("Gungaming instance already exists! Something is very wrong!");
        instance = this;
        logger = getLogger();
        ElementManager.INSTANCE.registerElements(
                "cz.jeme.programu.gungaming.item",
                "cz.jeme.programu.gungaming.loot.crate"
        );
        registerCommands();
        try {
            Class.forName(ResourcePackEventHandler.class.getName()); // load resource pack hash
        } catch (final ClassNotFoundException ignored) {
        }
        Bukkit.getPluginManager().registerEvents(EventDistributor.INSTANCE, this);
        final long initDuration = System.currentTimeMillis() - start;
        logger.info("Successfully enabled. (took %s ms)".formatted(initDuration));
    }

    @SuppressWarnings("UnstableApiUsage")
    private void registerCommands() {
        logger().info("Registering commands...");
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            GGCommand.register(this, commands);
        });
    }

    @Override
    public void onDisable() {
        ZoomManager.INSTANCE.zoomOutAll();
        ReloadManager.INSTANCE.abortReloadAll(false);
        CrateGenerator.INSTANCE.removeCrates(null);
        if (Lobby.enabled()) Lobby.instance().disable();
        if (Game.running()) Game.instance().stopGame();
    }

    public static @NotNull GunGaming plugin() {
        return Objects.requireNonNull(instance, "GunGaming has not yet been initialized!");
    }

    public static @NotNull NamespacedKey namespaced(final @KeyPattern.Value @NotNull String key) {
        return new NamespacedKey(NAMESPACE, key);
    }

    public static @NotNull Logger logger() {
        return Objects.requireNonNull(logger, "GunGaming has not yet been initialized!");
    }
}
