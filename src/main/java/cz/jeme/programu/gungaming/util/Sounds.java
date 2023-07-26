package cz.jeme.programu.gungaming.util;

import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.item.ammo.TwelveGauge;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.util.item.Guns;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.inventory.ItemStack;

public final class Sounds {
    private Sounds() {
        // Static class cannot be initialized
    }

    public static Sound getSound(String name, float volume, float pitch) {
        return Sound.sound(Key.key("gungaming", name), Sound.Source.MASTER, volume, pitch);
    }

    public static Sound getSound(String name, float volume) {
        return getSound(name, volume, 1f);
    }

    public static Sound getGunShootSound(Gun gun) {
        return getSound("gun.shoot." + formatGunName(gun), Float.MAX_VALUE);
    }

    public static Sound getGunSwitchSound(Gun gun) {
        return getSound("gun.switch." + formatGunName(gun), Float.MAX_VALUE);
    }

    public static Sound getGunReloadSound(ItemStack gunItem) {
        Gun gun = Guns.getGun(gunItem);
        float pitch = 1.0f;
        if (gun.reloadCooldown != (int) Namespaces.GUN_RELOAD_COOLDOWN.get(gunItem) && !gun.ammoType.equals(TwelveGauge.class)) {
            pitch = 1.2f - (((int) Namespaces.GUN_RELOAD_COOLDOWN.get(gunItem) * 100f) / gun.reloadCooldown - 100f) / 100f;
        }

        return getSound("gun.reload." + formatGunName(gun), Float.MAX_VALUE, pitch);
    }

    private static String formatGunName(Gun gun) {
        return gun.name.toLowerCase().replace(' ', '_');
    }
}
