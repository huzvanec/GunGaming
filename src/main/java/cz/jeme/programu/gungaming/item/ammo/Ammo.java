package cz.jeme.programu.gungaming.item.ammo;

import cz.jeme.programu.gungaming.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Ammo extends CustomItem {

    public Ammo() {
        addTags("ammo");
    }

    @Override
    protected final @NotNull Material provideMaterial() {
        return Material.WHITE_DYE;
    }

    public static @NotNull Ammo of(final @NotNull String keyStr) {
        return CustomItem.of(keyStr, Ammo.class);
    }

    public static @NotNull Ammo of(final @NotNull ItemStack item) {
        return CustomItem.of(item, Ammo.class);
    }

    public static boolean is(final @NotNull String keyStr) {
        return CustomItem.is(keyStr, Ammo.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Ammo.class);
    }
}
