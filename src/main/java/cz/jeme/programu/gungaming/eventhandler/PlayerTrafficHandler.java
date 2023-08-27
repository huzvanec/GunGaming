package cz.jeme.programu.gungaming.eventhandler;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.util.Messages;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class PlayerTrafficHandler {
    private static final @NotNull String RESOURCEPACK_MESSAGE =
            "<bold><dark_blue>____________________________________________________________</dark_blue>\n\n" +
                    "<dark_aqua>Hello! Welcome to <dark_red>GunGaming</dark_red>!</dark_aqua></bold>\n\n" +
                    "<dark_aqua>" +
                    Messages.latin("To play here, we need you to download this resourcepack!\n" +
                            "Without it you wouldn't get the awesome gaming expirience!") +
                    "</dark_aqua>\n" +
                    "<bold><dark_blue>____________________________________________________________</dark_blue></bold>";
    private static final @NotNull String VERSION = "BETAv2.2";
    private static final @NotNull String RESOURCEPACK_URL = "https://github.com/Mandlemankiller/GunGaming/releases/download/" + VERSION + "/resource-pack.zip";
    private static final @NotNull String HASH_URL = "https://github.com/Mandlemankiller/GunGaming/releases/download/" + VERSION + "/resource-pack.hash";
    private static final @NotNull String HASH;

    static {
        String tempHash;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(HASH_URL);
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            tempHash = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            GunGaming.serverLog("Couldn't download resourcepack hash!", e);
            tempHash = "";
        }
        HASH = tempHash;
    }

    private PlayerTrafficHandler() {
        throw new AssertionError();
    }

    public static void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setResourcePack(RESOURCEPACK_URL, HASH, true, Messages.from(RESOURCEPACK_MESSAGE));
    }

    public static void onPlayerResourcePackStatus(@NotNull PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
            player.kick(Messages.from("<#FF0000>You can't decline the resource pack!</#FF0000>"));
            return;
        }
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
            player.kick(Messages.from("<#FF0000>Failed to load resource pack!</#FF0000>"));
            return;
        }
    }
}
