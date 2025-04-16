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
public class TitaniumHelmet extends Helmet {
    @Override
    protected double provideArmor() {
        return 3;
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
        return 407;
    }

    @Override
    protected Key provideEquipSound() {
        return SoundEventKeys.ITEM_ARMOR_EQUIP_NETHERITE;
    }

    @Override
    protected String provideDescription() {
        return "helmet forged from raw titanium";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "titanium_helmet";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected Component provideName() {
        return Component.text("Titanium Helmet");
    }

}
