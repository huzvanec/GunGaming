package cz.jeme.gungaming.item.armor.impl;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.armor.Chestplate;
import cz.jeme.gungaming.loot.Rarity;
import io.papermc.paper.registry.keys.SoundEventKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TitaniumChestplate extends Chestplate {
    @Override
    protected double provideArmor() {
        return 8;
    }

    @Override
    protected double provideToughness() {
        return 2;
    }

    @Override
    protected Key provideArmorKey() {
        return GunGaming.key("titanium");
    }

    @Override
    protected int provideDurability() {
        return 592;
    }

    @Override
    protected Key provideEquipSound() {
        return SoundEventKeys.ITEM_ARMOR_EQUIP_NETHERITE;
    }

    @Override
    protected String provideDescription() {
        return "chestplate forged from raw titanium";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "titanium_chestplate";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected Component provideName() {
        return Component.text("Titanium Chestplate");
    }

}
