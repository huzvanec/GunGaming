package cz.jeme.programu.gungaming.util;

import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.item.Guns;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public final class Lores {
    private static final DecimalFormat FORMATTER = new DecimalFormat("#0.##");

    private Lores() {
    }

    public static void update(ItemMeta meta) {
        if (meta == null) throw new IllegalArgumentException("Meta is null!");
        List<Component> lore = new ArrayList<>();
        Rarity rarity = Rarity.valueOf(Namespaces.RARITY.get(meta));
        lore.add(Messages.from("<!italic><bold>" + rarity.name + "</bold></!italic>"));
        lore.add(Messages.from("<!italic><#CADCFF>" + Namespaces.INFO.get(meta) + "</#CADCFF></!italic>"));
        if (Namespaces.GUN.has(meta)) {
            Gun gun = Guns.getGun((String) Namespaces.GUN.get(meta));
            lore.add(Messages.from("<!italic><#CADCFF>" + Messages.latin("Damage: ") + FORMATTER.format(gun.damage) + "</#CADCFF></!italic>"));
            lore.add(Messages.from("<!italic><#CADCFF>" + Messages.latin("DPS: ") + calcDPS(gun.damage, gun.shootCooldown) + "</#CADCFF></!italic>"));

            lore.add(Messages.from(""));

            int currentAmmo = Namespaces.CURRENT_GUN_AMMO.get(meta);
            int maxAmmo = Namespaces.MAX_GUN_AMMO.get(meta);
            lore.add(Messages.from("<!italic><#77A5FF>Ammo: " + calcAmmo(currentAmmo, maxAmmo) + "</#77A5FF></!italic>"));
        }
        meta.lore(lore);
    }

    public static void update(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        update(meta);
        item.setItemMeta(meta);
    }

    private static String calcAmmo(int currentAmmo, int maxAmmo) {
        double phase = currentAmmo / (double) maxAmmo;
        return "<transition:#FF0000:#1FFF00:" + phase + ">" + currentAmmo + "/" + maxAmmo + "</transition>";
    }

    private static String calcDPS(double damage, int shootCooldown) {
        double DPS = damage / (shootCooldown / 1000D);
        return FORMATTER.format(DPS);
    }
}
