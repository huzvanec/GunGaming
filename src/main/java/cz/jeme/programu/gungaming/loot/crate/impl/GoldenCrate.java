package cz.jeme.programu.gungaming.loot.crate.impl;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.Weapon;
import cz.jeme.programu.gungaming.item.attachment.Attachment;
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

public class GoldenCrate extends Crate {
    @Override
    protected @NotNull Component provideName() {
        return Component.text("Golden Crate");
    }

    @Override
    protected @NotNull Map<Rarity, Integer> provideRarityChances() {
        return Map.of(
                Rarity.COMMON, 5,
                Rarity.UNCOMMON, 5,
                Rarity.RARE, 5,
                Rarity.EPIC, 9,
                Rarity.LEGENDARY, 8
        );
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.REPEATING_COMMAND_BLOCK;
    }

    @Override
    protected double provideFillPercentage() {
        return .65;
    }

    @Override
    protected double provideSpawnPercentage() {
        return .00006;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "golden_crate";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
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
                Weapon.class, 2,
                Attachment.class, 2
        );
    }
}