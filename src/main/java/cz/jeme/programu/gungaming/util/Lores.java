package cz.jeme.programu.gungaming.util;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.item.attachment.magazine.Magazine;
import cz.jeme.programu.gungaming.item.attachment.scope.Scope;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.item.throwable.Throwable;
import cz.jeme.programu.gungaming.item.throwable.grenade.MIRVGrenade;
import cz.jeme.programu.gungaming.item.throwable.grenade.SmallGrenade;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.item.Attachments;
import cz.jeme.programu.gungaming.util.item.Guns;
import cz.jeme.programu.gungaming.util.item.Throwables;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public final class Lores {
    private static final @NotNull DecimalFormat FORMATTER = new DecimalFormat("#0.##");

    private Lores() {
        // Static class cannot be initialized
    }

    public static void update(@NotNull ItemMeta meta) {
        List<Component> lore = new ArrayList<>();
        Rarity rarity = Rarity.valueOf(Namespace.RARITY.get(meta));
        lore.add(Message.from("<!italic><bold>" + rarity.name + "</bold></!italic>"));
        lore.add(Message.from("<!italic><#90FFF1>" + Namespace.INFO.get(meta) + "</#90FFF1></!italic>"));

        if (Namespace.GUN.has(meta)) {
            updateGun(meta, lore);
        } else if (Namespace.ATTACHMENT.has(meta)) {
            updateAttachment(meta, lore);
        } else if (Namespace.THROWABLE.has(meta)) {
            updateThrowable(meta, lore);
        }

        meta.lore(lore);
    }

    private static void updateAttachment(@NotNull ItemMeta meta, @NotNull List<Component> lore) {
        String name = Namespace.ATTACHMENT.get(meta);
        assert name != null : "Attachment name is null!";
        Attachment attachment = Attachments.getAttachment(name);
        assert attachment != null : "Attachment is null!";
        lore.addAll(attachment.modifiersInfo);
    }

    private static void updateGun(@NotNull ItemMeta meta, @NotNull List<Component> lore) {
        String name = Namespace.GUN.get(meta);
        assert name != null : "Gun name is null!";
        Gun gun = Guns.getGun(name);
        assert gun != null : "Gun is null!";
        lore.add(Message.from("<!italic><#CADCFF><#77A5FF>" + Message.latin("Damage: ") + "</#77A5FF>" + FORMATTER.format(gun.damage) + "</#CADCFF></!italic>"));
        lore.add(Message.from("<!italic><#CADCFF><#77A5FF>" + Message.latin("DPS: ") + "</#77A5FF>" + calcDPS(gun.damage, gun.shootCooldown, gun.bulletsPerShot) + "</#CADCFF></!italic>"));
        lore.add(Message.from("<!italic><#CADCFF><#77A5FF>" + Message.latin("Ammo type: ") + "</#77A5FF>" + gun.ammo.name + "</#CADCFF></!italic>"));

        lore.add(Message.from(""));

        Integer currentAmmo = Namespace.GUN_AMMO_CURRENT.get(meta);
        Integer maxAmmo = Namespace.GUN_AMMO_MAX.get(meta);
        assert currentAmmo != null : "Current ammo is null!";
        assert maxAmmo != null : "Max ammo is null!";
        String ammo = "<!italic><#77A5FF>" + Message.latin("Ammo: ") + "</#77A5FF>" + calcAmmo(currentAmmo, maxAmmo);

        String magazineName = Namespace.GUN_MAGAZINE.get(meta);
        assert magazineName != null : "Magazine name is null!";
        if (!magazineName.isEmpty()) {
            Magazine magazine = (Magazine) Attachments.getAttachment(magazineName);
            assert magazine != null : "Magazine is null!";
            float multiplier = magazine.magazinePercentage / 100f;
            int addedAmmo = Math.round(gun.maxAmmo * multiplier - gun.maxAmmo);
            ammo = ammo + " <#6FFD90>(+" + addedAmmo + " " + Message.latin(magazine.name) + ")</#6FFD90>";
        }

        lore.add(Message.from(ammo + "</!italic>"));

        String scopeName = Namespace.GUN_SCOPE.get(meta);
        assert scopeName != null : "Scope name is null!";
        if (!scopeName.isEmpty()) {
            Scope scope = (Scope) Attachments.getAttachment(scopeName);
            assert scope != null : "Scope is null!";
            String scopeLevel = FORMATTER.format(scope.scope);
            lore.add(Message.from("<!italic><#CADCFF><#77A5FF>" + Message.latin("Scope: ") + "</#77A5FF>" + scopeLevel + "× <#6FFD90>(+" + scopeLevel
                    + " " + Message.latin(scopeName) + ")</#6FFD90></#CADCFF></!italic>"));
        }
    }

    private static void updateThrowable(@NotNull ItemMeta meta, @NotNull List<Component> lore) {
        String name = Namespace.THROWABLE.get(meta);
        assert name != null : "Name is null!";
        Throwable throwable = Throwables.getThrowable(name);
        assert throwable != null : "Throwable is null!";
        String damage = FORMATTER.format(throwable.damage);
        if (throwable instanceof MIRVGrenade) {
            damage += " + 8×" + FORMATTER.format(SmallGrenade.DAMAGE);
        }
        lore.add(Message.from("<!italic><#CADCFF><#77A5FF>" + Message.latin("Damage: ") + "</#77A5FF>" + damage + "</#CADCFF></!italic>"));
    }

    public static void update(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        update(meta);
        item.setItemMeta(meta);
    }

    private static @NotNull String calcAmmo(int currentAmmo, int maxAmmo) {
        double phase = currentAmmo / (double) maxAmmo;
        if (phase > 1) phase = 1;
        return "<transition:#FF0000:#1FFF00:" + phase + ">" + currentAmmo + "/" + maxAmmo + "</transition>";
    }

    private static @NotNull String calcDPS(double damage, int shootCooldown, int bulletsPerShot) {
        double DPS = (damage / (shootCooldown / 1000D)) * bulletsPerShot;
        return FORMATTER.format(DPS);
    }
}
