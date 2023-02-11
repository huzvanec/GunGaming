package cz.jeme.programu.gungaming.utils;

import java.util.HashMap;
import java.util.Map;

public class LatinUtils {
	public static Map<Character, Character> latin = new HashMap<Character, Character>();

	static {
		latin.put('a', 'ᴀ');
		latin.put('b', 'ʙ');
		latin.put('c', 'ᴄ');
		latin.put('d', 'ᴅ');
		latin.put('e', 'ᴇ');
		latin.put('f', 'ғ');
		latin.put('g', 'ɢ');
		latin.put('h', 'ʜ');
		latin.put('i', 'ɪ');
		latin.put('j', 'ᴊ');
		latin.put('k', 'ᴋ');
		latin.put('l', 'ʟ');
		latin.put('m', 'ᴍ');
		latin.put('n', 'ɴ');
		latin.put('o', 'ᴏ');
		latin.put('p', 'ᴘ');
		latin.put('q', 'ǫ');
		latin.put('r', 'ʀ');
		latin.put('s', 's');
		latin.put('t', 'ᴛ');
		latin.put('u', 'ᴜ');
		latin.put('v', 'ᴠ');
		latin.put('w', 'ᴡ');
		latin.put('x', 'x');
		latin.put('y', 'ʏ');
		latin.put('z', 'z');

	}
	
	private LatinUtils() {
		// Only static utils
	}

	public static String toLatin(String str) {
		str = str.toLowerCase();
		StringBuffer buffer = new StringBuffer(100);
		for (int i = 0; i < str.length(); i++) {
			char character = str.charAt(i);
			character = toLatin(character);
			buffer.append(character);
		}
		return buffer.toString();
	}

	public static char toLatin(char character) {
		if (latin.containsKey(character)) {
			return latin.get(character);
		}
		return character;
	}

}
