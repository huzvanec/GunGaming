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
public abstract class Stock extends Attachment {
    public static final PersistentData<String, String> GUN_STOCK_KEY_DATA = PersistentData.ofString(GunGaming.key("gun_stock_key"));
    private static final ItemStack PLACEHOLDER = PlaceholderHelper.placeholder(
            GunGaming.key("stock_placeholder"),
            meta -> meta.displayName(Components.of("<!i><gray>Stock"))
    );

    public static ItemStack placeholder(final ItemStack gunItem) {
        return PLACEHOLDER.clone();
    }

    protected Stock() {
        addTags("stock");
    }

    protected final double recoilMultiplier = provideRecoilMultiplier();

    protected abstract double provideRecoilMultiplier();

    public final double recoilMultiplier() {
        return recoilMultiplier;
    }

    @Override
    public void apply(final HumanEntity player, final ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        Gun.RECOIL_DATA.write(gunItem, gun.recoil() * recoilMultiplier);
    }

    @Override
    public void remove(final HumanEntity player, final ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        Gun.RECOIL_DATA.write(gunItem, gun.recoil());
    }

    public static Stock of(final String keyStr) {
        return CustomElement.of(keyStr, Stock.class);
    }

    public static Stock of(final ItemStack item) {
        return CustomItem.of(item, Stock.class);
    }

    public static boolean is(final String keyStr) {
        return CustomElement.is(keyStr, Stock.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Stock.class);
    }
}
