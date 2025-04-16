package cz.jeme.gungaming.item.gun;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.item.attachment.*;
import cz.jeme.gungaming.item.attachment.impl.Silencer;
import cz.jeme.gungaming.util.Components;
import cz.jeme.gungaming.util.Lores;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public final class StatsMenu {
    private final HumanEntity player;
    private final ItemStack gunItem;
    private final ItemStack gunItemBackup;
    private final Gun gun;
    private final Inventory inventory;

    public StatsMenu(final HumanEntity player, final ItemStack gunItem) {
        this.player = player;
        this.gunItem = gunItem;
        gunItemBackup = gunItem.clone();
        gun = Gun.of(gunItem);
        inventory = Bukkit.createInventory(
                player,
                InventoryType.HOPPER,
                gun.strippedName().append(Component.text(" statistics"))
        );

        fill();

        player.openInventory(inventory);
        player.playSound(gun.heldSound(gunItem), player);
    }

    @SuppressWarnings("UnstableApiUsage")
    private static ItemStack statItem(final Key key, final String name, final List<String> lore) {
        final ItemStack item = ItemStack.of(Material.WHITE_STAINED_GLASS_PANE);
        item.editMeta(meta -> {
            meta.displayName(Components.of("<!i>" + name));
            meta.lore(lore.stream()
                    .map(str -> Components.of("<!i><white>" + str))
                    .toList());
        });
        item.setData(DataComponentTypes.ITEM_MODEL, key);
        return item;
    }

    private static String loreStat(final String key, final String value, final @Nullable String modification) {
        if (modification == null) return loreStat(key, value);
        return "<#77A5FF>" + Components.latinString(key) + ": <st><#A3ABBB>" + value + "</st> " + modification;
    }

    private static String loreStat(final String key, final String value) {
        return "<#77A5FF>" + Components.latinString(key) + ": <#CADCFF>" + value;
    }

    private static @Nullable String loreModification(final String prefix, final @Nullable String value, final @Nullable Attachment attachment) {
        if (value == null || attachment == null) return null;
        return prefix + value + " (" + Components.latinString(Components.strip(attachment.name())) + ")";
    }

    private void fill() {
        // basic info
        final String nameStr = loreStat("Name", Components.strip(gun.name()));
        final String descriptionStr = loreStat("Description", gun.description());
        final Scope scope = Scope.GUN_SCOPE_KEY_DATA.read(gunItem)
                .map(Scope::of)
                .orElse(null);
        final String scopeStr = loreStat(
                "Scope",
                "0×",
                loreModification(
                        "<green>",
                        scope == null
                                ? null
                                : Lores.STATS_FORMATTER.format(scope.zoom()) + "×",
                        scope
                )
        );
        final String magazinelessStr = loreStat("Magazineless", String.valueOf(gun.magazineless()));
        final ItemStack basicInfo = statItem(
                GunGaming.key("basic_info_icon"),
                "Basic Info",
                List.of(nameStr, descriptionStr, scopeStr, magazinelessStr)
        );
        inventory.setItem(0, basicInfo);
        final boolean shotgun = gun.shotgun();
        final int realBulletsPerShot = shotgun ? gun.bulletsPerShot() : 1;
        // damage
        final double damage = Gun.DAMAGE_DATA.require(gunItem);
        final Silencer silencer = Silencer.GUN_SILENCER_KEY_DATA.read(gunItem)
                .map(key -> CustomItem.of(Silencer.class))
                .orElse(null);
        final String damageStr = loreStat(
                "Per Bullet",
                Lores.STATS_FORMATTER.format(gun.damage()),
                loreModification(
                        "<red>",
                        Lores.STATS_FORMATTER.format(damage),
                        silencer
                )
        );
        final double shootCooldownSeconds = Gun.SHOOT_COOLDOWN_DATA.require(gunItem) / 20D;
        final String dpsStr = loreStat(
                "Per Second",
                Lores.STATS_FORMATTER.format(gun.damage() / shootCooldownSeconds * gun.bulletsPerShot()),
                loreModification(
                        "<red>",
                        Lores.STATS_FORMATTER.format(damage / shootCooldownSeconds * gun.bulletsPerShot()),
                        silencer
                )
        );
        final String damageShotStr = loreStat(
                "Per Shot",
                Lores.STATS_FORMATTER.format(gun.damage() * realBulletsPerShot),
                loreModification(
                        "<red>",
                        Lores.STATS_FORMATTER.format(damage * realBulletsPerShot),
                        silencer
                )
        );
        final ItemStack damageInfo = statItem(
                GunGaming.key("damage_icon"),
                "Damage",
                List.of(damageStr, damageShotStr, dpsStr)
        );
        inventory.setItem(1, damageInfo);
        // durations
        final String shootDurationStr = loreStat("Shooting", Lores.STATS_FORMATTER.format(
                Gun.SHOOT_COOLDOWN_DATA.require(gunItem) / 20D / (shotgun ? 1 : gun.bulletsPerShot())
        ) + "s");
        final Magazine magazine = Magazine.GUN_MAGAZINE_KEY_DATA.read(gunItem)
                .map(Magazine::of)
                .orElse(null);
        final String reloadDurationStr = loreStat(
                "Reloading",
                Lores.STATS_FORMATTER.format(gun.reloadDuration() / 20D * realBulletsPerShot) + "s",
                loreModification(
                        "<red>",
                        Lores.STATS_FORMATTER.format(Gun.RELOAD_DURATION_DATA.require(gunItem) / 20D * realBulletsPerShot) + "s",
                        magazine
                )
        );
        final ItemStack durationInfo = statItem(
                GunGaming.key("durations_icon"),
                "Durations",
                List.of(shootDurationStr, reloadDurationStr)
        );
        inventory.setItem(2, durationInfo);
        // stability
        final int recoilModifier = shotgun ? gun.bulletsPerShot() : 1;
        final String recoilStr = loreStat(
                "Recoil",
                Lores.STATS_FORMATTER.format(gun.recoil() * recoilModifier * 10),
                loreModification(
                        "<green>",
                        Lores.STATS_FORMATTER.format(Gun.RECOIL_DATA.require(gunItem) * recoilModifier * 10),
                        Stock.GUN_STOCK_KEY_DATA.read(gunItem)
                                .map(Stock::of)
                                .orElse(null)
                )
        );
        final Grip grip = Grip.GUN_GRIP_KEY_DATA.read(gunItem)
                .map(Grip::of)
                .orElse(null);
        final String spreadStr = loreStat(
                "Spread",
                Lores.STATS_FORMATTER.format(gun.inaccuracy()),
                loreModification(
                        "<green>",
                        Lores.STATS_FORMATTER.format(Gun.INACCURACY_DATA.require(gunItem)),
                        grip
                )
        );
        final ItemStack stabilityInfo = statItem(
                GunGaming.key("stability_icon"),
                "Stability",
                List.of(recoilStr, spreadStr)
        );
        inventory.setItem(3, stabilityInfo);
        // ammo
        final String ammoTypeStr = loreStat("Type", Components.strip(gun.ammo().name()));
        final String currentAmmoStr = loreStat("Current Ammo", String.valueOf(Gun.CURRENT_AMMO_DATA.require(gunItem)));
        final String maxAmmoStr = loreStat(
                "Max Ammo",
                String.valueOf(gun.maxAmmo()),
                loreModification(
                        "<green>",
                        String.valueOf(Gun.MAX_AMMO_DATA.require(gunItem)),
                        magazine
                )
        );
        final String bulletSpeedStr = loreStat(
                "Bullet Speed",
                Lores.STATS_FORMATTER.format(Gun.BULLET_VELOCITY_DATA.require(gunItem))
        );
        final ItemStack ammoInfo = statItem(
                GunGaming.key("ammo_icon"),
                "Ammo",
                List.of(ammoTypeStr, currentAmmoStr, maxAmmoStr, bulletSpeedStr)
        );
        inventory.setItem(4, ammoInfo);
    }

    void inventoryClick(final InventoryClickEvent event) {
        if (event.getClickedInventory() == inventory)
            event.setCancelled(true);
    }

    void playerDropItem(final PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().equals(gunItemBackup))
            player.closeInventory();
    }


    public Inventory inventory() {
        return inventory;
    }
}
