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

import java.util.List;

@NullMarked
public abstract class Grip extends Attachment {
    public static final PersistentData<String, String> GUN_GRIP_KEY_DATA = PersistentData.ofString(GunGaming.key("gun_grip_key"));
    private static final ItemStack PLACEHOLDER = PlaceholderHelper.placeholder(
            GunGaming.key("grip_placeholder"),
            meta -> meta.displayName(Components.of("<!i><gray>Grip"))
    );

    private static final ItemStack SHOTGUN_PLACEHOLDER = PlaceholderHelper.placeholder(
            GunGaming.key("grip_shotgun_placeholder"),
            meta -> {
                meta.displayName(Components.of("<!i><gray>Grip"));
                meta.lore(List.of(Components.of("<!i><red>" + Components.latinString("Grips have only quarter effects on shotguns!"))));
            }
    );

    public static ItemStack placeholder(final ItemStack gunItem) {
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
    public void apply(final HumanEntity player, final ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        Gun.INACCURACY_DATA.write(gunItem, gun.inaccuracy() * inaccuracyMultiplier);
    }

    @Override
    public void remove(final HumanEntity player, final ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        Gun.INACCURACY_DATA.write(gunItem, gun.inaccuracy());
    }

    public static Grip of(final String keyStr) {
        return CustomElement.of(keyStr, Grip.class);
    }

    public static Grip of(final ItemStack item) {
        return CustomItem.of(item, Grip.class);
    }

    public static boolean is(final String keyStr) {
        return CustomElement.is(keyStr, Grip.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Grip.class);
    }
}
