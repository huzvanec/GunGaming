package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.command.gg.GGCommand;
import cz.jeme.programu.gungaming.data.Data;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.util.Components;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Magazine extends Attachment {
    public static final @NotNull Data<String, String> GUN_MAGAZINE_KEY_DATA = Data.ofString(GunGaming.namespaced("gun_magazine_key"));
    private static final @NotNull ItemStack PLACEHOLDER = PlaceholderHelper.placeholder(meta -> {
        meta.displayName(Components.of("<!i><gray>Magazine"));
        meta.setCustomModelData(4);
    });

    public static @NotNull ItemStack placeholder(final @NotNull ItemStack gunItem) {
        return PLACEHOLDER.clone();
    }

    protected final double maxAmmoMultiplier = provideMaxAmmoMultiplier();
    protected final double reloadDurationMultiplier = provideReloadDurationMultiplier();

    protected abstract double provideMaxAmmoMultiplier();

    protected double provideReloadDurationMultiplier() {
        return maxAmmoMultiplier;
    }

    protected Magazine() {
        addTags("magazine");
    }

    public final double maxAmmoMultiplier() {
        return maxAmmoMultiplier;
    }

    public final double reloadDurationMultiplier() {
        return reloadDurationMultiplier;
    }

    @Override
    public void apply(final @NotNull HumanEntity player, final @NotNull ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        Gun.MAX_AMMO_DATA.write(gunItem, (int) Math.round(gun.maxAmmo() * maxAmmoMultiplier));
        Gun.RELOAD_DURATION_DATA.write(gunItem, (int) Math.round(gun.reloadDuration() * reloadDurationMultiplier));
    }

    @Override
    public void remove(final @NotNull HumanEntity player, final @NotNull ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        final int maxAmmo = gun.maxAmmo();
        Gun.MAX_AMMO_DATA.write(gunItem, maxAmmo);
        Gun.RELOAD_DURATION_DATA.write(gunItem, gun.reloadDuration());
        final int currentAmmo = Gun.CURRENT_AMMO_DATA.require(gunItem);
        if (currentAmmo <= maxAmmo) return;
        Gun.setAmmo(gunItem, maxAmmo);
        GGCommand.give(player, gun.ammo().item(), currentAmmo - maxAmmo);
    }

    public static @NotNull Magazine of(final @NotNull String keyStr) {
        return CustomElement.of(keyStr, Magazine.class);
    }

    public static @NotNull Magazine of(final @NotNull ItemStack item) {
        return CustomItem.of(item, Magazine.class);
    }

    public static boolean is(final @NotNull String keyStr) {
        return CustomElement.is(keyStr, Magazine.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Magazine.class);
    }
}
