package cz.jeme.programu.gungaming.item.tool;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.loot.SingleLoot;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;

public abstract class Tool extends CustomItem implements SingleLoot {
    protected final double attackSpeed = provideAttackSpeed();

    @SuppressWarnings("UnstableApiUsage")
    protected Tool() {
        addTags("tool");
        item.editMeta(meta -> meta.addAttributeModifier(
                Attribute.GENERIC_ATTACK_SPEED,
                new AttributeModifier(
                        GunGaming.namespaced(key.value() + "_generic_attack_speed"),
                        attackSpeed,
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlotGroup.MAINHAND
                )
        ));
    }

    protected double provideAttackSpeed() {
        return -3;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 1;
    }
}
