package cz.jeme.programu.gungaming.item.attachment.impl;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.data.Data;
import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.item.attachment.PlaceholderHelper;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Silencer extends Attachment {
    public static final @NotNull Data<String, String> GUN_SILENCER_KEY_DATA = Data.ofString(GunGaming.namespaced("gun_silencer_key"));
    private static final @NotNull ItemStack PLACEHOLDER = PlaceholderHelper.placeholder(meta -> {
        meta.displayName(Components.of("<!i><gray>Silencer"));
        meta.setCustomModelData(1);
    });

    public static @NotNull ItemStack placeholder(final @NotNull ItemStack gunItem) {
        return PLACEHOLDER.clone();
    }

    protected final double volumeMultiplier = provideVolumeMultiplier();
    protected final double damageMultiplier = provideDamageMultiplier();

    protected double provideVolumeMultiplier() {
        return .05;
    }

    protected double provideDamageMultiplier() {
        return .95;
    }

    @Override
    protected @NotNull List<String> provideBuffs() {
        return List.of(
                "-95% gunshot volume"
        );
    }

    @Override
    protected @NotNull List<String> provideDebuffs() {
        return List.of(
                "-5% damage"
        );
    }

    public final double volumeMultiplier() {
        return volumeMultiplier;
    }

    public final double damageMultiplier() {
        return damageMultiplier;
    }

    @Override
    public void apply(final @NotNull HumanEntity player, final @NotNull ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        Gun.DAMAGE_DATA.write(gunItem, gun.damage() * damageMultiplier);
    }

    @Override
    public void remove(final @NotNull HumanEntity player, final @NotNull ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        Gun.DAMAGE_DATA.write(gunItem, gun.damage());
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "silencer";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Silencer");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Quietens gunfire";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }
}
