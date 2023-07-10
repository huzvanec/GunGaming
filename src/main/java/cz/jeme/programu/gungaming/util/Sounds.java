package cz.jeme.programu.gungaming.util;

import cz.jeme.programu.gungaming.item.gun.Gun;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

public final class Sounds {
    private Sounds() {
        // Static class cannot be initialized
    }

    public static Sound getSound(String name, float volume) {
        return Sound.sound(Key.key("gungaming", name), Sound.Source.MASTER, volume, 1f);
    }

    public static Sound getGunShootSound(Gun gun) {
        return getSound("gun.shoot." + formatGunName(gun), 100);
    }

    public static Sound getGunReloadSound(Gun gun) {
        return getSound("gun.reload." + formatGunName(gun), 100);
    }

    private static String formatGunName(Gun gun) {
        return gun.name.toLowerCase().replace(' ', '_');
    }
}
