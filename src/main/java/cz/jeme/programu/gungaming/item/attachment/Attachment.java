package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.util.Components;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Every class extending {@link Attachment} must include a static {@code placeholder(ItemStack gunItem)} method.
 */
public abstract class Attachment extends CustomItem {

    protected final @NotNull List<String> buffs = Collections.unmodifiableList(provideBuffs());
    protected final @NotNull List<String> debuffs = Collections.unmodifiableList(provideDebuffs());
    protected @NotNull List<String> modifiers = new ArrayList<>();

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
    protected @NotNull List<String> update(final @NotNull ItemStack item) {
        return new ArrayList<>(modifiers);
    }


    // providers

    protected abstract @NotNull List<String> provideBuffs();

    protected abstract @NotNull List<String> provideDebuffs();


    // getters

    public final @NotNull List<String> buffs() {
        return buffs;
    }

    public final @NotNull List<String> debuffs() {
        return debuffs;
    }

    // override stuff

    @Override
    protected final @NotNull Material provideMaterial() {
        return Material.BLACK_DYE;
    }

    @Override
    protected final @NotNull String provideType() {
        return "attachment";
    }

    @Override
    protected final int provideMinAmount() {
        return 1;
    }

    @Override
    protected final int provideMaxAmount() {
        return 1;
    }

    public void apply(final @NotNull HumanEntity player, final @NotNull ItemStack gunItem) {
    }

    public void remove(final @NotNull HumanEntity player, final @NotNull ItemStack gunItem) {
    }

    // static accessors

    public static @NotNull Attachment of(final @NotNull String keyStr) {
        return CustomElement.of(keyStr, Attachment.class);
    }

    public static @NotNull Attachment of(final @NotNull ItemStack item) {
        return CustomItem.of(item, Attachment.class);
    }

    public static boolean is(final @NotNull String keyStr) {
        return CustomElement.is(keyStr, Attachment.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Attachment.class);
    }

}
