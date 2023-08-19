package cz.jeme.programu.gungaming.eventhandler;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.Resourcepacks;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

public class PlayerJoinHandler {

    private final @NotNull File dataFolder;

    public PlayerJoinHandler(@NotNull File dataFolder) {
        this.dataFolder = dataFolder;
    }

    private static final @NotNull String RESOURCEPACK_URL = "https://github.com/Mandlemankiller/GunGaming/releases/latest/download/resourcepack.zip";
    private static final @NotNull String RESOURCEPACK_MESSAGE =
            "<bold><dark_blue>____________________________________________________________</dark_blue>\n\n" +
                    "<dark_aqua>Hello! Welcome to <dark_red>GunGaming</dark_red>!</dark_aqua></bold>\n\n" +
                    "<dark_aqua>" +
                    Messages.latin("To play here, we need you to download this resourcepack!\n" +
                            "Without it you wouldn't get the awesome gaming expirience!") +
                    "</dark_aqua>\n" +
                    "<bold><dark_blue>____________________________________________________________</dark_blue></bold>";

    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try {
            player.setResourcePack(RESOURCEPACK_URL, Resourcepacks.generateSHA1(RESOURCEPACK_URL, dataFolder),
                    Messages.from(RESOURCEPACK_MESSAGE), true);
        } catch (NoSuchAlgorithmException | IOException e) {
            GunGaming.serverLog(Level.SEVERE, "Couldn't apply player resourcepack!");
            e.printStackTrace();
        }
    }
}
