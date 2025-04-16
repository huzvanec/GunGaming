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
import org.jspecify.annotations.NullMarked;

import java.util.Map;

@NullMarked
public class AirDrop extends Crate {
    @Override
    protected Map<Rarity, Integer> provideRarityChances() {
        return Map.of(
                Rarity.RARE, 1,
                Rarity.EPIC, 3,
                Rarity.LEGENDARY, 6
        );
    }

    @Override
    protected Material provideMaterial() {
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
    protected @KeyPattern.Value String provideKey() {
        return "air_drop";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected Component provideName() {
        return Component.text("Air Drop");
    }

    @Override
    protected Map<Class<? extends CustomItem>, Integer> provideLimits() {
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
