package cz.jeme.programu.gungaming.eventhandler;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.util.Message;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public final class PlayerTrafficHandler {
    private static final @NotNull String RESOURCE_PACK_MESSAGE =
            "<b><dark_blue>____________________________________________________________</dark_blue>\n\n" +
                    "<dark_aqua>Hello! Welcome to <dark_red>GunGaming</dark_red>!</dark_aqua></b>\n\n" +
                    "<dark_aqua>" +
                    Message.latin("To play here, we need you to download this resource pack!\n" +
                            "Without it you wouldn't get the awesome gaming experience!") +
                    "</dark_aqua>\n" +
                    "<b><dark_blue>____________________________________________________________</dark_blue></b>";
    private static final @NotNull String VERSION = "BETAv3.0";
    private static final @NotNull String RESOURCE_PACK_URL = "https://github.com/Mandlemankiller/GunGaming/releases/download/" + VERSION + "/resource-pack.zip";
    private static final @NotNull String HASH_URL = "https://github.com/Mandlemankiller/GunGaming/releases/download/" + VERSION + "/resource-pack.hash";
    private static final @NotNull String HASH;

    static {
        String tempHash = "";
        try {
            URL url = new URL(HASH_URL);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            tempHash = reader.readLine();
            reader.close();
        } catch (IOException e) {
            GunGaming.serverLog("Couldn't download resource pack hash!", e);
        }
        HASH = tempHash;
    }

    private PlayerTrafficHandler() {
        throw new AssertionError();
    }

    public static void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
        player.setResourcePack(RESOURCE_PACK_URL, HASH, true, Message.from(RESOURCE_PACK_MESSAGE));
    }

    public static void onPlayerResourcePackStatus(@NotNull PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
            player.kick(Message.from("<#FF0000>You can't decline the resource pack!</#FF0000>"));
            return;
        }
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
            player.kick(Message.from("<#FF0000>Failed to load resource pack!</#FF0000>"));
            return;
        }
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    public static void onPlayerMove(@NotNull PlayerMoveEvent event) {
        if (!event.getPlayer().hasResourcePack()) {
            event.setCancelled(true);
        }
    }
}
