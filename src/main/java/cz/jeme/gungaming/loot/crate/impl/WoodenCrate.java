package cz.jeme.gungaming.loot.crate.impl;

import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.item.armor.Armor;
import cz.jeme.gungaming.item.attachment.Attachment;
import cz.jeme.gungaming.item.gun.Gun;
import cz.jeme.gungaming.item.melee.Melee;
import cz.jeme.gungaming.loot.Rarity;
import cz.jeme.gungaming.loot.crate.Crate;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.CommandBlock;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

@NullMarked
public class WoodenCrate extends Crate {
    @Override
    protected @KeyPattern.Value String provideKey() {
        return "wooden_crate";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    protected Component provideName() {
        return Component.text("Wooden Crate");
    }

    @Override
    protected Map<Rarity, Integer> provideRarityChances() {
        return Map.of(
                Rarity.COMMON, 22,
                Rarity.UNCOMMON, 19,
                Rarity.RARE, 16,
                Rarity.EPIC, 3,
                Rarity.LEGENDARY, 1
        );
    }

    @Override
    protected Material provideMaterial() {
        return Material.CHAIN_COMMAND_BLOCK;
    }

    @Override
    protected double provideFillPercentage() {
        return .3;
    }

    @Override
    protected double provideSpawnPercentage() {
        return .0007;
    }

    @Override
    protected void generated(final Block block, final Inventory inventory) {
        final CommandBlock data = (CommandBlock) block.getBlockData();
        data.setConditional(true);
        block.setBlockData(data);
    }

    @Override
    protected Map<Class<? extends CustomItem>, Integer> provideLimits() {
        return Map.of(
                Gun.class, 1,
                Melee.class, 1,
                Attachment.class, 1,
                Armor.class, 1
        );
    }
}