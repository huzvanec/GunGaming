package cz.jeme.programu.gungaming.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class Components {
    private Components() {
        throw new AssertionError();
    }

    private static final @NotNull Map<Character, Character> LATIN = new HashMap<>();

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
//        LATIN.put('1', '₁');
//        LATIN.put('2', '₂');
//        LATIN.put('3', '₃');
//        LATIN.put('4', '₄');
//        LATIN.put('5', '₅');
//        LATIN.put('6', '₆');
//        LATIN.put('7', '₇');
//        LATIN.put('8', '₈');
//        LATIN.put('9', '₉');
//        LATIN.put('0', '₉');
    }

    public static final @NotNull String PREFIX = "<dark_gray>[<b><#6786C8>ɢ</#6786C8><#4C618D>ɢ</#4C618D></b>]</dark_gray> ";

    public static @NotNull Component of(final @NotNull String string) {
        return MiniMessage.miniMessage().deserialize(string);
    }

    public static @NotNull String toString(final @NotNull Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }

    public static @NotNull Component prefix(final @NotNull String string) {
        return Components.of(PREFIX + string);
    }

    public static @NotNull Component latin(final @NotNull String string) {
        return Components.of(latinString(string));
    }

    public static @NotNull String latinString(final @NotNull String string) {
        final StringBuilder builder = new StringBuilder();
        for (final char character : string.toCharArray())
            builder.append(latinChar(character));
        return builder.toString();
    }

    public static char latinChar(final char character) {
        return LATIN.getOrDefault(Character.toLowerCase(character), character);
    }

    public static @NotNull String strip(final @NotNull String string) {
        return MiniMessage.miniMessage().stripTags(string);
    }

    public static @NotNull String strip(final @NotNull Component component) {
        return strip(toString(component));
    }
}
