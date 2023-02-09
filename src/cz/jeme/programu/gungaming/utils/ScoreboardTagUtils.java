package cz.jeme.programu.gungaming.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Entity;

public final class ScoreboardTagUtils {

	private static final String SEPARATOR = ":=:";
	public static final String HEADER = "GunGaming";

	private ScoreboardTagUtils() {
		// Only static utils
	}

	public static Set<String> mapToStringSet(Map<String, String> map) {
		Set<String> set = new HashSet<String>();

		for (String key : map.keySet()) {
			String value = map.get(key);
			set.add(key + SEPARATOR + value);
		}
		return set;
	}

	public static Map<String, String> stringSetToMap(Set<String> set) {
		if (!set.contains(HEADER)) {
			return new HashMap<String, String>();
		}

		Map<String, String> map = new HashMap<String, String>();

		for (String item : set) {
			String[] splitArray = item.split(SEPARATOR);
			if (splitArray.length != 2) {
				continue;
			}
			map.put(splitArray[0], splitArray[1]);
		}
		return map;
	}

	public static void addScoreboardTags(Entity entity, Set<String> set) {
		if (!entity.getScoreboardTags().contains(HEADER)) {
			customTag(entity);
		}
		for (String item : set) {
			entity.addScoreboardTag(item);
		}
	}

	public static void addScoreboardTags(Entity entity, Map<String, String> map) {
		addScoreboardTags(entity, mapToStringSet(map));
	}

	public static void customTag(Entity entity) {
		entity.addScoreboardTag(HEADER);
	}

	public static Map<String, String> getScoreboardTags(Entity entity) {
		return stringSetToMap(entity.getScoreboardTags());
	}

	public static boolean isCustomTagged(Entity entity) {
		return entity.getScoreboardTags().contains(HEADER);
	}
}
