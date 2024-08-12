package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.attachment.*;
import cz.jeme.programu.gungaming.item.attachment.impl.Silencer;
import cz.jeme.programu.gungaming.util.Components;
import cz.jeme.programu.gungaming.util.Lores;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class StatsMenu {
    private final @NotNull HumanEntity player;
    private final @NotNull ItemStack gunItem;
    private final @NotNull ItemStack gunItemBackup;
    private final @NotNull Gun gun;
    private final @NotNull Inventory inventory;

    public StatsMenu(final @NotNull HumanEntity player, final @NotNull ItemStack gunItem) {
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

    private static @NotNull ItemStack statItem(final @NotNull String name, final @NotNull List<String> lore, final @Nullable Integer customModelData) {
        final ItemStack item = ItemStack.of(Material.WHITE_STAINED_GLASS_PANE);
        item.editMeta(meta -> {
            meta.displayName(Components.of("<!i>" + name));
            meta.lore(lore.stream()
                    .map(str -> Components.of("<!i><white>" + str))
                    .toList());
            meta.setCustomModelData(customModelData);
        });
        return item;
    }

    private static @NotNull String loreStat(final @NotNull String key, final @NotNull String value, final @Nullable String modification) {
        if (modification == null) return loreStat(key, value);
        return "<#77A5FF>" + Components.latinString(key) + ": <st><#A3ABBB>" + value + "</st> " + modification;
    }

    private static @NotNull String loreStat(final @NotNull String key, final @NotNull String value) {
        return "<#77A5FF>" + Components.latinString(key) + ": <#CADCFF>" + value;
    }

    private static @Nullable String loreModification(final @NotNull String prefix, final @Nullable String value, final @Nullable Attachment attachment) {
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
                "Basic Info",
                List.of(nameStr, descriptionStr, scopeStr, magazinelessStr),
                8
        );
        inventory.setItem(0, basicInfo);
        final boolean shotgun = gun.shotgun();
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
        final ItemStack damageInfo = statItem(
                "Damage",
                List.of(damageStr, dpsStr),
                9
        );
        inventory.setItem(1, damageInfo);
        // durations
        final String shootDurationStr = loreStat("Shooting", Lores.STATS_FORMATTER.format(
                Gun.SHOOT_COOLDOWN_DATA.require(gunItem) / 20D / (shotgun ? 1 : gun.bulletsPerShot())
        ) + "s");
        final int reloadModifier = shotgun ? gun.bulletsPerShot() : 1;
        final Magazine magazine = Magazine.GUN_MAGAZINE_KEY_DATA.read(gunItem)
                .map(Magazine::of)
                .orElse(null);
        final String reloadDurationStr = loreStat(
                "Reloading",
                Lores.STATS_FORMATTER.format(gun.reloadDuration() / 20D * reloadModifier) + "s",
                loreModification(
                        "<red>",
                        Lores.STATS_FORMATTER.format(Gun.RELOAD_DURATION_DATA.require(gunItem) / 20D * reloadModifier) + "s",
                        magazine
                )
        );
        final ItemStack durationInfo = statItem(
                "Durations",
                List.of(shootDurationStr, reloadDurationStr),
                10
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
                "Stability",
                List.of(recoilStr, spreadStr),
                11
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
                "Ammo",
                List.of(ammoTypeStr, currentAmmoStr, maxAmmoStr, bulletSpeedStr),
                12
        );
        inventory.setItem(4, ammoInfo);
    }

    void inventoryClick(final @NotNull InventoryClickEvent event) {
        if (event.getClickedInventory() == inventory)
            event.setCancelled(true);
    }

    void playerDropItem(final @NotNull PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().equals(gunItemBackup))
            player.closeInventory();
    }


    public @NotNull Inventory inventory() {
        return inventory;
    }
}
