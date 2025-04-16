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
public class GoldenCrate extends Crate {
    @Override
    protected Component provideName() {
        return Component.text("Golden Crate");
    }

    @Override
    protected Map<Rarity, Integer> provideRarityChances() {
        return Map.of(
                Rarity.COMMON, 1,
                Rarity.UNCOMMON, 1,
                Rarity.RARE, 1,
                Rarity.EPIC, 1,
                Rarity.LEGENDARY, 1
        );
    }

    @Override
    protected Material provideMaterial() {
        return Material.REPEATING_COMMAND_BLOCK;
    }

    @Override
    protected double provideFillPercentage() {
        return .65;
    }

    @Override
    protected double provideSpawnPercentage() {
        return .00007;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "golden_crate";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.EPIC;
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
                Gun.class, 2,
                Melee.class, 1,
                Attachment.class, 2,
                Armor.class, 1
        );
    }
}
