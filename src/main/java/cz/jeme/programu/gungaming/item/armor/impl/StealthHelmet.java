package cz.jeme.programu.gungaming.item.armor.impl;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.armor.Helmet;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StealthHelmet extends Helmet {
    private static final @NotNull String DISPLAY_NAME = "Stealth Helmet";
    public static final @NotNull Component WARNING = Components.of("<red>You are wearing a " + DISPLAY_NAME + "!");

    protected StealthHelmet() {
        item.editMeta(Damageable.class, meta -> meta.setMaxDamage(10));
    }

    @Override
    protected double provideArmor() {
        return 0;
    }

    @Override
    protected double provideToughness() {
        return 0;
    }

    @Override
    protected @NotNull List<String> update(final @NotNull ItemStack item) {
        return List.of();
    }

    @Override
    protected @NotNull String provideDescription() {
        return "makes you untrackable and unable to track";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.GOLDEN_HELMET;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "stealth_helmet";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text(DISPLAY_NAME);
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
    }

    public static boolean hasEquipped(final @NotNull Player player) {
        return CustomItem.is(player.getInventory().getHelmet(), StealthHelmet.class);
    }
}