package cz.jeme.gungaming.item.ammo;

import cz.jeme.gungaming.CustomElement;
import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.CustomItem;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class Ammo extends CustomItem {

    public Ammo() {
        addTags("ammo");
    }

    @Override
    protected final Material provideMaterial() {
        return Material.POPPED_CHORUS_FRUIT;
    }

    @Override
    protected final String provideType() {
        return "ammo";
    }

    protected final Sound heldSound = Sound.sound(GunGaming.key("item.ammo.held"), Sound.Source.PLAYER, 1.9F, 1);

    @Override
    public Sound heldSound(final ItemStack item) {
        return heldSound;
    }

    public static Ammo of(final String keyStr) {
        return CustomElement.of(keyStr, Ammo.class);
    }

    public static Ammo of(final ItemStack item) {
        return CustomItem.of(item, Ammo.class);
    }

    public static boolean is(final String keyStr) {
        return CustomElement.is(keyStr, Ammo.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Ammo.class);
    }
}
