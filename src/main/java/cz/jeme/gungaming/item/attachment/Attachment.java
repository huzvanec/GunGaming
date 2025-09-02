package cz.jeme.gungaming.item.attachment;

import cz.jeme.gungaming.CustomElement;
import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.util.Components;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Every class extending {@link Attachment} must include a static {@code placeholder(ItemStack gunItem)} method.
 */
@NullMarked
public abstract class Attachment extends CustomItem {

    protected final List<String> buffs = Collections.unmodifiableList(provideBuffs());
    protected final List<String> debuffs = Collections.unmodifiableList(provideDebuffs());
    protected final List<String> modifiers = new ArrayList<>();

    protected Attachment() {
        addTags("attachment");
        for (final String buff : buffs)
            modifiers.add("<green>" + Components.latinString(buff));
        for (final String debuff : debuffs)
            modifiers.add("<red>" + Components.latinString(debuff));
        item.editMeta(meta -> meta.setMaxStackSize(1));
    }

    // lore

    @Override
    protected List<String> update(final ItemStack item) {
        return new ArrayList<>(modifiers);
    }


    // providers

    protected abstract List<String> provideBuffs();

    protected abstract List<String> provideDebuffs();


    // getters

    public final List<String> buffs() {
        return buffs;
    }

    public final List<String> debuffs() {
        return debuffs;
    }

    // override stuff

    @Override
    protected final Material provideMaterial() {
        return Material.POPPED_CHORUS_FRUIT;
    }

    @Override
    protected final String provideType() {
        return "attachment";
    }

    protected final Sound heldSound = Sound.sound(GunGaming.key("item.attachment.held"), Sound.Source.PLAYER, 1.9F, 1);

    @Override
    public Sound heldSound(final ItemStack item) {
        return heldSound;
    }

    @Override
    protected final int provideMinAmount() {
        return 1;
    }

    @Override
    protected final int provideMaxAmount() {
        return 1;
    }

    public void apply(final HumanEntity player, final ItemStack gunItem) {
    }

    public void remove(final HumanEntity player, final ItemStack gunItem) {
    }

    // static accessors

    public static Attachment of(final String keyStr) {
        return CustomElement.of(keyStr, Attachment.class);
    }

    public static Attachment of(final ItemStack item) {
        return CustomItem.of(item, Attachment.class);
    }

    public static boolean is(final String keyStr) {
        return CustomElement.is(keyStr, Attachment.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Attachment.class);
    }

}
