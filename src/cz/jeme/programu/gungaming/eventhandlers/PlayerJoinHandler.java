package cz.jeme.programu.gungaming.eventhandlers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.utils.LatinUtils;
import cz.jeme.programu.gungaming.utils.ResourcepackUtils;
import net.md_5.bungee.api.ChatColor;

public class PlayerJoinHandler {
	private static final String RESOURCEPACK_URL = "https://skladu.jeme.cz/GunGaming.zip";
	private static final String RESOURCEPACK_MESSAGE = ChatColor.DARK_BLUE + ChatColor.BOLD.toString()
			+ "____________________________________________________________\n\n" + ChatColor.DARK_AQUA
			+ ChatColor.BOLD.toString() + " Hello! Welcome to " + ChatColor.DARK_RED + ChatColor.BOLD.toString()
			+ "GunGaming" + ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "!\n\n" + ChatColor.DARK_AQUA
			+ LatinUtils.toLatin("To play here, we need you to download this resourcepack!\n"
					+ "Without it you wouldn't get the awesome gaming expirience!\n\n")
			+ ChatColor.DARK_BLUE + ChatColor.BOLD.toString()
			+ "____________________________________________________________";

	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		try {
			player.setResourcePack(RESOURCEPACK_URL, ResourcepackUtils.generateSHA1(RESOURCEPACK_URL),
					RESOURCEPACK_MESSAGE, true);
		} catch (NoSuchAlgorithmException | IOException e) {
			GunGaming.serverLog(Level.SEVERE, e.getStackTrace().toString());
		}
	}
}
