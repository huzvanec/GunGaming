package cz.jeme.programu.gungaming.item.ammo;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.CustomItem;
import net.kyori.adventure.sound.Sound;
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

    @Override
    protected final @NotNull String provideType() {
        return "ammo";
    }

    protected final @NotNull Sound heldSound = Sound.sound(GunGaming.namespaced("item.ammo.held"), Sound.Source.PLAYER, 1.9F, 1);

    @Override
    public @NotNull Sound heldSound(final @NotNull ItemStack item) {
        return heldSound;
    }

    public static @NotNull Ammo of(final @NotNull String keyStr) {
        return CustomElement.of(keyStr, Ammo.class);
    }

    public static @NotNull Ammo of(final @NotNull ItemStack item) {
        return CustomItem.of(item, Ammo.class);
    }

    public static boolean is(final @NotNull String keyStr) {
        return CustomElement.is(keyStr, Ammo.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Ammo.class);
    }
}
