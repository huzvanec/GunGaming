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

import java.util.List;

public abstract class Grip extends Attachment {
    public static final @NotNull Data<String, String> GUN_GRIP_KEY_DATA = Data.ofString(GunGaming.namespaced("gun_grip_key"));
    private static final @NotNull ItemStack PLACEHOLDER = PlaceholderHelper.placeholder(meta -> {
        meta.displayName(Components.of("<!i><gray>Grip"));
        meta.setCustomModelData(2);
    });

    private static final @NotNull ItemStack SHOTGUN_PLACEHOLDER = PlaceholderHelper.placeholder(meta -> {
        meta.displayName(Components.of("<!i><gray>Grip"));
        meta.setCustomModelData(6);
        meta.lore(List.of(Components.of("<!i><red>Grips have only quarter effects on shotguns!")));
    });

    public static @NotNull ItemStack placeholder(final @NotNull ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        final ItemStack placeholder = gun.shotgun() ? SHOTGUN_PLACEHOLDER : PLACEHOLDER;
        return placeholder.clone();
    }

    protected Grip() {
        addTags("grip");
    }

    protected final double inaccuracyMultiplier = provideInaccuracyMultiplier();

    protected abstract double provideInaccuracyMultiplier();

    public final double inaccuracyMultiplier() {
        return inaccuracyMultiplier;
    }

    @Override
    public void apply(final @NotNull HumanEntity player, final @NotNull ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        Gun.INACCURACY_DATA.write(gunItem, gun.inaccuracy() * inaccuracyMultiplier);
    }

    @Override
    public void remove(final @NotNull HumanEntity player, final @NotNull ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        Gun.INACCURACY_DATA.write(gunItem, gun.inaccuracy());
    }

    public static @NotNull Grip of(final @NotNull String keyStr) {
        return CustomElement.of(keyStr, Grip.class);
    }

    public static @NotNull Grip of(final @NotNull ItemStack item) {
        return CustomItem.of(item, Grip.class);
    }

    public static boolean is(final @NotNull String keyStr) {
        return CustomElement.is(keyStr, Grip.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Grip.class);
    }
}
