package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.command.GGCommand;
import cz.jeme.programu.gungaming.game.Game;
import cz.jeme.programu.gungaming.item.attachment.ZoomManager;
import cz.jeme.programu.gungaming.item.gun.ReloadManager;
import cz.jeme.programu.gungaming.loot.crate.CrateGenerator;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.logging.Logger;


public final class GunGaming extends JavaPlugin {
    private static @Nullable GunGaming instance;
    public static final @NotNull String NAMESPACE = "gungaming";
    @SuppressWarnings("NotNullFieldNotInitialized")
    private static @NotNull Logger logger;

    @Override
    public void onEnable() {
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
    }

    @SuppressWarnings("UnstableApiUsage")
    private void registerCommands() {
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
        if (!Game.running()) return;
        final Game game = Objects.requireNonNull(Game.instance());
        for (final Player player : Bukkit.getOnlinePlayers()) {
            player.activeBossBars().forEach(player::hideBossBar);
            game.team().removePlayer(player);
        }
    }

    public static @NotNull GunGaming plugin() {
        return Objects.requireNonNull(instance, "GunGaming has not yet been initialized!");
    }

    public static @NotNull NamespacedKey namespaced(final @KeyPattern.Value @NotNull String key) {
        return new NamespacedKey(NAMESPACE, key);
    }

    public static @NotNull Logger logger() {
        return logger;
    }
}
