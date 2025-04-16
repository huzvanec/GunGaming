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
public class IronLeggings extends Leggings {
    @Override
    protected double provideArmor() {
        return 4;
    }

    @Override
    protected Key provideArmorKey() {
        return GunGaming.key("iron");
    }

    @Override
    protected int provideDurability() {
        return 225;
    }

    @Override
    protected Key provideEquipSound() {
        return SoundEventKeys.ITEM_ARMOR_EQUIP_IRON;
    }

    @Override
    protected String provideDescription() {
        return "basic leggings";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "iron_leggings";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected Component provideName() {
        return Component.text("Iron Leggings");
    }
}
