package cz.jeme.programu.gungaming.utils;

import cz.jeme.programu.gungaming.items.guns.Gun;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GunUtils {

    public static Map<String, Gun> guns = new HashMap<>();

    private GunUtils() {
        // Only static utils
    }

    public static boolean isGun(ItemStack item) {
        if (item == null) {
            return false;
        }
        if (item.getItemMeta() == null) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        for (Gun gun : guns.values()) {
            ItemStack gunItem = gun.item;
            ItemMeta gunMeta = gunItem.getItemMeta();

            if (gunItem.getType() != item.getType()) {
                continue;
            }
            assert gunMeta != null;
            if (!gunMeta.getDisplayName().equals(meta.getDisplayName())) {
                continue;
            }
            if (gunMeta.getCustomModelData() != meta.getCustomModelData()) {
                continue;
            }
            return true;
        }
        return false;
    }

    public static Gun getGun(String name) {
        return guns.get(name);
    }

    public static Gun getGun(ItemStack item) {
        if (item == null) {
            return null;
        }
        if (!item.hasItemMeta()) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        String name = meta.getDisplayName();
        return getGun(ChatColor.stripColor(name));
    }

    public static void setUnmodifiableGuns() {
        guns = Collections.unmodifiableMap(guns);
    }

    public static void register(Gun gun) {
        guns.put(ChatColor.stripColor(gun.name), gun);
    }
}
