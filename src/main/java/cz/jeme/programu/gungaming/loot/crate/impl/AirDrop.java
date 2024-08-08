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
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AirDrop extends Crate {
    @Override
    protected @NotNull Map<Rarity, Integer> provideRarityChances() {
        return Map.of(
                Rarity.RARE, 1,
                Rarity.EPIC, 3,
                Rarity.LEGENDARY, 6
        );
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.CHAIN_COMMAND_BLOCK;
    }

    @Override
    protected double provideFillPercentage() {
        return .95;
    }

    @Override
    protected double provideSpawnPercentage() {
        return 0;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "air_drop";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Air Drop");
    }

    @Override
    protected @NotNull Map<Class<? extends CustomItem>, Integer> provideLimits() {
        return Map.of(
                Attachment.class, 3,
                Gun.class, 3,
                Melee.class, 2,
                Armor.class, 3
        );
    }

    @Override
    protected boolean provideModifyContents() {
        return false;
    }
}
