package cz.jeme.programu.gungaming.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class Message {
    public static final @NotNull Map<Character, Character> LATIN = new HashMap<>();

    static {
        LATIN.put('a', 'ᴀ');
        LATIN.put('b', 'ʙ');
        LATIN.put('c', 'ᴄ');
        LATIN.put('d', 'ᴅ');
        LATIN.put('e', 'ᴇ');
        LATIN.put('f', 'ꜰ');
        LATIN.put('g', 'ɢ');
        LATIN.put('h', 'ʜ');
        LATIN.put('i', 'ɪ');
        LATIN.put('j', 'ᴊ');
        LATIN.put('k', 'ᴋ');
        LATIN.put('l', 'ʟ');
        LATIN.put('m', 'ᴍ');
        LATIN.put('n', 'ɴ');
        LATIN.put('o', 'ᴏ');
        LATIN.put('p', 'ᴘ');
        LATIN.put('q', 'ǫ');
        LATIN.put('r', 'ʀ');
        LATIN.put('s', 's');
        LATIN.put('t', 'ᴛ');
        LATIN.put('u', 'ᴜ');
        LATIN.put('v', 'ᴠ');
        LATIN.put('w', 'ᴡ');
        LATIN.put('x', 'x');
        LATIN.put('y', 'ʏ');
        LATIN.put('z', 'z');
    }

    public static final @NotNull MiniMessage MESSAGE = MiniMessage.miniMessage();

    // Plugin prefix
    // [ɢɢ]:
    public static final @NotNull String PREFIX = "<dark_gray>[<bold><#485C86>ɢɢ</#485C86></bold>] </dark_gray>";

    private Message() {
        // Static class cannot be initialized
    }

    public static @NotNull Component from(@NotNull String text) {
        return MESSAGE.deserialize(text);
    }

    public static @NotNull Component prefix(@NotNull String text) {
        return MESSAGE.deserialize(PREFIX + text);
    }

    public static @NotNull Component prefix(@NotNull Component component) {
        return from(PREFIX).append(component);
    }

    public static @NotNull String strip(@NotNull String text) {
        return MESSAGE.stripTags(text);
    }

    public static @NotNull String strip(@NotNull Component component) {
        return strip(to(component));
    }

    public static @NotNull String to(@NotNull Component component) {
        return MESSAGE.serialize(component);
    }

    public static @NotNull String latin(@NotNull String string) {
        string = string.toLowerCase();
        StringBuilder builder = new StringBuilder(100);
        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);
            character = latin(character);
            builder.append(character);
        }
        return builder.toString();
    }

    public static @NotNull String getEscapeTag(@NotNull String string) {
        return string.replace("<", "</");
    }

    public static char latin(char character) {
        if (LATIN.containsKey(character)) {
            return LATIN.get(character);
        }
        return character;
    }
}