package cz.jeme.programu.gungaming.loot.crate.impl;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.armor.Armor;
import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.item.melee.Melee;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.loot.crate.Crate;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.CommandBlock;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class WoodenCrate extends Crate {
    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "wooden_crate";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Wooden Crate");
    }

    @Override
    protected @NotNull Map<Rarity, Integer> provideRarityChances() {
        return Map.of(
                Rarity.COMMON, 14,
                Rarity.UNCOMMON, 12,
                Rarity.RARE, 8,
                Rarity.EPIC, 3,
                Rarity.LEGENDARY, 1
        );
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.CHAIN_COMMAND_BLOCK;
    }

    @Override
    protected double provideFillPercentage() {
        return .3;
    }

    @Override
    protected double provideSpawnPercentage() {
        return .0005;
    }

    @Override
    protected void generated(final @NotNull Block block, final @NotNull Inventory inventory) {
        final CommandBlock data = (CommandBlock) block.getBlockData();
        data.setConditional(true);
        block.setBlockData(data);
    }

    @Override
    protected @NotNull Map<Class<? extends CustomItem>, Integer> provideLimits() {
        return Map.of(
                Gun.class, 1,
                Melee.class, 1,
                Attachment.class, 1,
                Armor.class, 1
        );
    }
}