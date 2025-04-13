package cz.jeme.gungaming;

import cz.jeme.gungaming.util.Components;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

@NullMarked
public final class ResourcePackEventHandler {
    private static final Component RESOURCE_PACK_MESSAGE = Components.of("""
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

    @SuppressWarnings("UnstableApiUsage")
    private static final String VERSION = "v" + GunGaming.instance().getPluginMeta().getVersion();
    private static final String REPO_DOWNLOAD = "https://github.com/huzvanec/GunGaming/releases/download/" + VERSION;
    private static final String RESOURCE_PACK_URL = REPO_DOWNLOAD + "/resource-pack.zip";
    private static final String RESOURCE_PACK_HASH_URL = REPO_DOWNLOAD + "/resource-pack.sha1";
    private static @Nullable String resourcePackHash;

    static {
        GunGaming.logger().info("Downloading resource pack hash...");
        try {
            final URL url = new URI(RESOURCE_PACK_HASH_URL).toURL();
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                resourcePackHash = reader.readLine();
            }
        } catch (final URISyntaxException | IOException e) {
            GunGaming.instance().getLogger().log(
                    Level.SEVERE,
                    "Failed to download resource pack hash; resource pack will not be served to clients",
                    e
            );
            resourcePackHash = null; // just for clarity
        }
    }

    private ResourcePackEventHandler() {
        throw new AssertionError();
    }

    private static final Set<UUID> LOADING_PLAYERS = new HashSet<>();

    public static void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (resourcePackHash == null) {
            GunGaming.logger().warning(
                    "Server failed to load resource pack on startup, so it won't be sent to player '" + player.getName() + "'"
            );
            return;
        }
        LOADING_PLAYERS.add(player.getUniqueId());
        player.setResourcePack(
                RESOURCE_PACK_URL,
                resourcePackHash,
                true,
                RESOURCE_PACK_MESSAGE
        );
    }

    public static void onPlayerMove(final PlayerMoveEvent event) {
        if (LOADING_PLAYERS.contains(event.getPlayer().getUniqueId()))
            event.setCancelled(true);
    }

    public static void onPlayerInteract(final PlayerInteractEvent event) {
        if (LOADING_PLAYERS.contains(event.getPlayer().getUniqueId()))
            event.setCancelled(true);
    }

    public static void onEntityDamage(final EntityDamageEvent event) {
        if (LOADING_PLAYERS.contains(event.getEntity().getUniqueId()))
            event.setCancelled(true);
    }

    public static void onPlayerResourcePackStatus(final PlayerResourcePackStatusEvent event) {
        final Player player = event.getPlayer();
        switch (event.getStatus()) {
            case SUCCESSFULLY_LOADED -> LOADING_PLAYERS.remove(player.getUniqueId());
            case DOWNLOADED, ACCEPTED -> {
                // only status updates, not results
            }
            default -> {
                LOADING_PLAYERS.remove(player.getUniqueId());
                player.kick(Components.of("<#FF0000>Failed to load resource pack: " + event.getStatus()));
            }
        }
    }
}
