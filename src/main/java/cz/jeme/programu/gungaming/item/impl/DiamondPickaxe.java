package cz.jeme.programu.gungaming.item.impl;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.loot.SingleLoot;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

public class DiamondPickaxe extends CustomItem implements SingleLoot {


    @SuppressWarnings("UnstableApiUsage")
    protected DiamondPickaxe() {
        item.editMeta(meta -> meta.addAttributeModifier(
                Attribute.GENERIC_ATTACK_SPEED,
                new AttributeModifier(
                        GunGaming.namespaced(key.value() + "_generic_attack_speed"),
                        -3,
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlotGroup.MAINHAND
                )
        ));
    }

    @Override
    protected @NotNull String provideDescription() {
        return "great for breaking blocks in the way";
    }

    @Override
    protected @NotNull Material provideMaterial() {
        return Material.DIAMOND_PICKAXE;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 1;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "diamond_pickaxe";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Diamond Pickaxe");
    }
}
