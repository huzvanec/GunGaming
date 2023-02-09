package cz.jeme.programu.gungaming.utils;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class MessageUtils {

	private MessageUtils() {
		// Only static utils
	}

	public static void actionMessage(Player player, String message) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
	}

}
