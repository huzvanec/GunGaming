package cz.jeme.programu.gungaming.util;

import cz.jeme.programu.gungaming.item.attachment.magazine.Magazine;
import cz.jeme.programu.gungaming.item.attachment.scope.Scope;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.item.Attachments;
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
        lore.add(Messages.from("<!italic><#90FFF1>" + Namespaces.INFO.get(meta) + "</#90FFF1></!italic>"));
        if (Namespaces.GUN.has(meta)) {
            Gun gun = Guns.getGun((String) Namespaces.GUN.get(meta));
            lore.add(Messages.from("<!italic><#CADCFF><#77A5FF>" + Messages.latin("Damage: ") + "</#77A5FF>" + FORMATTER.format(gun.damage) + "</#CADCFF></!italic>"));
            lore.add(Messages.from("<!italic><#CADCFF><#77A5FF>" + Messages.latin("DPS: ") + "</#77A5FF>" + calcDPS(gun.damage, gun.shootCooldown) + "</#CADCFF></!italic>"));

            lore.add(Messages.from(""));

            int currentAmmo = Namespaces.CURRENT_GUN_AMMO.get(meta);
            int maxAmmo = Namespaces.MAX_GUN_AMMO.get(meta);
            String ammo = "<!italic><#77A5FF>" + Messages.latin("Ammo: ") + "</#77A5FF>" + calcAmmo(currentAmmo, maxAmmo);

            String magazineName = Namespaces.GUN_MAGAZINE.get(meta);
            if (!magazineName.equals("")) {
                Magazine magazine = (Magazine) Attachments.getAttachment(magazineName);
                float multiplier = magazine.magazinePercentage / 100f;
                int addedAmmo = Math.round(gun.maxAmmo * multiplier - gun.maxAmmo);
                ammo = ammo + " <#6FFD90>(+" + addedAmmo + " " + Messages.latin(magazine.name) + ")</#6FFD90>";
            }

            lore.add(Messages.from(ammo + "</!italic>"));

            String scopeName = Namespaces.GUN_SCOPE.get(meta);
            if (!scopeName.equals("")) {
                Scope scope = (Scope) Attachments.getAttachment(scopeName);
                String scopeLevel = FORMATTER.format(scope.scope);
                lore.add(Messages.from("<!italic><#CADCFF><#77A5FF>" + Messages.latin("Scope: ") + "</#77A5FF>" + scopeLevel + "× <#6FFD90>(+" + scopeLevel
                        + " " + Messages.latin(scopeName) + ")</#6FFD90></#CADCFF></!italic>"));
            }
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
        if (phase > 1) phase = 1;
        return "<transition:#FF0000:#1FFF00:" + phase + ">" + currentAmmo + "/" + maxAmmo + "</transition>";
    }

    private static String calcDPS(double damage, int shootCooldown) {
        double DPS = damage / (shootCooldown / 1000D);
        return FORMATTER.format(DPS);
    }
}
