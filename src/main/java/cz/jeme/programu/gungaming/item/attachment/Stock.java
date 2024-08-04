package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.data.Data;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.util.Components;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Stock extends Attachment {
    public static final @NotNull Data<String, String> GUN_STOCK_KEY_DATA = Data.ofString(GunGaming.namespaced("gun_stock_key"));
    private static final @NotNull ItemStack PLACEHOLDER = PlaceholderHelper.placeholder(meta -> {
        meta.displayName(Components.of("<!i><gray>Stock"));
        meta.setCustomModelData(5);
    });

    public static @NotNull ItemStack placeholder(final @NotNull ItemStack gunItem) {
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
    public void apply(final @NotNull HumanEntity player, final @NotNull ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        Gun.RECOIL_DATA.write(gunItem, gun.recoil() * recoilMultiplier);
    }

    @Override
    public void remove(final @NotNull HumanEntity player, final @NotNull ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        Gun.RECOIL_DATA.write(gunItem, gun.recoil());
    }

    public static @NotNull Stock of(final @NotNull String keyStr) {
        return CustomElement.of(keyStr, Stock.class);
    }

    public static @NotNull Stock of(final @NotNull ItemStack item) {
        return CustomItem.of(item, Stock.class);
    }

    public static boolean is(final @NotNull String keyStr) {
        return CustomElement.is(keyStr, Stock.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Stock.class);
    }
}
