package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ResourcePackEventHandler {
    private static final boolean RESOURCE_PACK_ENABLED = true;

    private static final @NotNull Component RESOURCE_PACK_MESSAGE = Components.of("""
                    <b><dark_blue>============================================================</b>
                    \s
                    <yellow>Welcome to <b><#6786C8>Gun</#6786C8><#4C618D>Gaming</#4C618D></b>!
                    \s
                    %s
                    %s
                    \s
                    <b><dark_blue>============================================================
                    """.formatted(
                    Components.latinString("To play here, we need you to download this resource pack."),
                    Components.latinString("Without it you wouldn't get the awesome gaming experience.")
            )
    );
    private static final @NotNull String VERSION = "v1.4";
    private static final @NotNull String REPO_DOWNLOAD = "https://github.com/huzvanec/GunGaming/releases/download/" + VERSION + "/";
    private static final @NotNull String RESOURCE_PACK_URL = REPO_DOWNLOAD + "resource-pack.zip";
    private static final @NotNull String RESOURCE_PACK_HASH_URL = REPO_DOWNLOAD + "resource-pack.sha1";
    private static final @NotNull String RESOURCE_PACK_HASH;

    static {
        GunGaming.logger().info("Downloading resource pack hash...");
        try {
            final URL url = new URI(RESOURCE_PACK_HASH_URL).toURL();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            RESOURCE_PACK_HASH = reader.readLine();
            reader.close();
        } catch (final URISyntaxException | IOException e) {
            throw new RuntimeException("Could not download resource pack hash!", e);
        }
    }

    private ResourcePackEventHandler() {
        throw new AssertionError();
    }

    private static final @NotNull Map<UUID, GameMode> LOADING_PLAYERS = new HashMap<>();

    public static void onPlayerJoin(final @NotNull PlayerJoinEvent event) {
        if (!RESOURCE_PACK_ENABLED) return;
        final Player player = event.getPlayer();
        LOADING_PLAYERS.put(player.getUniqueId(), player.getGameMode());
        player.setGameMode(GameMode.SPECTATOR);
        player.setResourcePack(
                RESOURCE_PACK_URL,
                RESOURCE_PACK_HASH,
                true,
                RESOURCE_PACK_MESSAGE
        );
    }

    public static void onPlayerMove(final @NotNull PlayerMoveEvent event) {
        if (LOADING_PLAYERS.containsKey(event.getPlayer().getUniqueId()))
            event.setCancelled(true);
    }

    public static void onPlayerResourcePackStatus(@NotNull final PlayerResourcePackStatusEvent event) {
        final Player player = event.getPlayer();
        switch (event.getStatus()) {
            case SUCCESSFULLY_LOADED -> {
                player.setGameMode(LOADING_PLAYERS.get(player.getUniqueId()));
                LOADING_PLAYERS.remove(player.getUniqueId());
            }
            case DECLINED, FAILED_RELOAD, FAILED_DOWNLOAD, DISCARDED, INVALID_URL -> {
                player.setGameMode(LOADING_PLAYERS.get(player.getUniqueId()));
                LOADING_PLAYERS.remove(player.getUniqueId());
                player.kick(Components.of("<#FF0000>Failed to load resource pack: " + event.getStatus()));
            }
        }
    }
}
