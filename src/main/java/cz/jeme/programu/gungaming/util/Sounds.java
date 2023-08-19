package cz.jeme.programu.gungaming.util;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.ammo.TwelveGauge;
import cz.jeme.programu.gungaming.item.consumable.Consumable;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.item.throwable.Throwable;
import cz.jeme.programu.gungaming.item.throwable.grenade.Grenade;
import cz.jeme.programu.gungaming.util.item.Guns;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class Sounds {
    private Sounds() {
        // Static class cannot be initialized
    }

    public static @NotNull Sound getSound(@NotNull String name, float volume, float pitch) {
        return Sound.sound(Key.key("gungaming", name), Sound.Source.MASTER, volume, pitch);
    }

    public static @NotNull Sound getSound(@NotNull String name, float volume) {
        return getSound(name, volume, 1f);
    }

    public static @NotNull Sound getGunShootSound(@NotNull Gun gun) {
        return getSound("gun.shoot." + formatName(gun), 6.3f);
    }

    public static @NotNull Sound getGunSwitchSound(@NotNull Gun gun) {
        return getSound("gun.switch." + formatName(gun), 1.9f);
    }

    public static @NotNull Sound getGunReloadSound(@NotNull ItemStack gunItem) {
        Gun gun = Guns.getGun(gunItem);
        float pitch = 1.0f;
        Integer reloadCooldown = Namespace.GUN_RELOAD_COOLDOWN.get(gunItem);
        assert reloadCooldown != null : "Reload cooldown is null!";
        if (gun.reloadCooldown != reloadCooldown.intValue() && !gun.ammoType.equals(TwelveGauge.class)) {
            pitch = 1.1f - ((reloadCooldown * 100f) / gun.reloadCooldown - 100f) / 100f;
        }

        return getSound("gun.reload." + formatName(gun), 2.5f, pitch);
    }

    public static @NotNull Sound getThrowableExplosionSound(@NotNull Throwable throwable) {
        return getSound("throwable.explosion." + formatName(throwable), 9.4f);
    }

    public static @NotNull Sound getThrowableThrowSound(@NotNull Throwable throwable) {
        if (throwable instanceof Grenade) return getSound("throwable.throw.grenade",1.9f);
        return getSound("throwable.throw." + formatName(throwable), 1.9f);
    }

    public static @NotNull Sound getConsumableEatSound(@NotNull Consumable consumable) {
        return getSound("consumable.eat." + formatName(consumable),  1f);
    }
    public static @NotNull Sound getConsumableBurpSound(@NotNull Consumable consumable) {
        return getSound("consumable.burp." + formatName(consumable),  1f);
    }

    public static @NotNull String formatName(@NotNull CustomItem customItem) {
        return customItem.name.toLowerCase().replace(' ', '_');
    }
}
