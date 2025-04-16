package cz.jeme.gungaming;

import cz.jeme.gungaming.command.ChatCommand;
import cz.jeme.gungaming.command.GGCommand;
import cz.jeme.gungaming.game.Game;
import cz.jeme.gungaming.game.lobby.Lobby;
import cz.jeme.gungaming.item.attachment.ZoomManager;
import cz.jeme.gungaming.item.gun.ReloadManager;
import cz.jeme.gungaming.loot.crate.CrateGenerator;
import cz.jeme.gungaming.util.Components;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.logging.Logger;


@NullMarked
public final class GunGaming extends JavaPlugin {
    private static final GunGaming INSTANCE = new GunGaming();
    public static final String NAMESPACE = INSTANCE.getName().toLowerCase(Locale.ROOT);

    private GunGaming() {
    }

    @Override
    public void onEnable() {
        final long start = System.currentTimeMillis();
        ElementManager.INSTANCE.registerElements(
                "cz.jeme.gungaming.item",
                "cz.jeme.gungaming.loot.crate"
        );

        registerCommands();

        try {
            Class.forName(ResourcePackEventHandler.class.getName()); // load resource pack hash
        } catch (final ClassNotFoundException e) {
            throw new AssertionError(e);
        }

        Bukkit.getPluginManager().registerEvents(EventDistributor.INSTANCE, this);

        getLogger().info("Successfully enabled (took %sms)".formatted(
                System.currentTimeMillis() - start
        ));

        System.out.println(Components.latinString("buff"));
        System.out.println(Components.latinString("nerf"));
    }

    @SuppressWarnings("UnstableApiUsage")
    private void registerCommands() {
        getLogger().info("Registering commands...");
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            new GGCommand(this, commands);
            new ChatCommand(this, commands);
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

    public static NamespacedKey key(final @KeyPattern.Value String key) {
        return new NamespacedKey(INSTANCE, key);
    }

    public static GunGaming instance() {
        return INSTANCE;
    }

    public static Logger logger() {
        return INSTANCE.getLogger();
    }
}
