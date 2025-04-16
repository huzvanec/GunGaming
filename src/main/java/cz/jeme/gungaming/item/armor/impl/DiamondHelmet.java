package cz.jeme.gungaming.item.armor.impl;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.armor.Helmet;
import cz.jeme.gungaming.loot.Rarity;
import io.papermc.paper.registry.keys.SoundEventKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DiamondHelmet extends Helmet {
    @Override
    protected double provideArmor() {
        return 2;
    }

    @Override
    protected double provideToughness() {
        return 1;
    }

    @Override
    protected Key provideArmorKey() {
        return GunGaming.key("diamond");
    }

    @Override
    protected int provideDurability() {
        return 363;
    }

    @Override
    protected Key provideEquipSound() {
        return SoundEventKeys.ITEM_ARMOR_EQUIP_DIAMOND;
    }

    @Override
    protected String provideDescription() {
        return "good helmet";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "diamond_helmet";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected Component provideName() {
        return Component.text("Diamond Helmet");
    }
}
