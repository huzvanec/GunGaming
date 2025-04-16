package cz.jeme.gungaming.item.armor.impl;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.armor.Leggings;
import cz.jeme.gungaming.loot.Rarity;
import io.papermc.paper.registry.keys.SoundEventKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DiamondLeggings extends Leggings {
    @Override
    protected double provideArmor() {
        return 5;
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
        return 495;
    }

    @Override
    protected Key provideEquipSound() {
        return SoundEventKeys.ITEM_ARMOR_EQUIP_DIAMOND;
    }

    @Override
    protected String provideDescription() {
        return "good leggings";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "diamond_leggings";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected Component provideName() {
        return Component.text("Diamond Leggings");
    }
}
