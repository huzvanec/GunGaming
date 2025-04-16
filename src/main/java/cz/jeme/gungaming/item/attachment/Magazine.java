package cz.jeme.gungaming.item.attachment;

import cz.jeme.gungaming.CustomElement;
import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.item.gun.Gun;
import cz.jeme.gungaming.persistence.PersistentData;
import cz.jeme.gungaming.util.Components;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class Magazine extends Attachment {
    public static final PersistentData<String, String> GUN_MAGAZINE_KEY_DATA = PersistentData.ofString(GunGaming.key("gun_magazine_key"));
    private static final ItemStack PLACEHOLDER = PlaceholderHelper.placeholder(
            GunGaming.key("magazine_placeholder")
            , meta -> meta.displayName(Components.of("<!i><gray>Magazine"))
    );

    public static ItemStack placeholder(final ItemStack gunItem) {
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
    public void apply(final HumanEntity player, final ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        Gun.MAX_AMMO_DATA.write(gunItem, (int) Math.round(gun.maxAmmo() * maxAmmoMultiplier));
        Gun.RELOAD_DURATION_DATA.write(gunItem, (int) Math.round(gun.reloadDuration() * reloadDurationMultiplier));
    }

    @Override
    public void remove(final HumanEntity player, final ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        final int maxAmmo = gun.maxAmmo();
        Gun.MAX_AMMO_DATA.write(gunItem, maxAmmo);
        Gun.RELOAD_DURATION_DATA.write(gunItem, gun.reloadDuration());
        final int currentAmmo = Gun.CURRENT_AMMO_DATA.require(gunItem);
        if (currentAmmo <= maxAmmo) return;
        Gun.setAmmo(gunItem, maxAmmo);
        player.getInventory().addItem(gun.ammo().item().asQuantity(currentAmmo - maxAmmo));
//        GGCommand.give(player, gun.ammo().item(), currentAmmo - maxAmmo);
    }

    public static Magazine of(final String keyStr) {
        return CustomElement.of(keyStr, Magazine.class);
    }

    public static Magazine of(final ItemStack item) {
        return CustomItem.of(item, Magazine.class);
    }

    public static boolean is(final String keyStr) {
        return CustomElement.is(keyStr, Magazine.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Magazine.class);
    }
}
