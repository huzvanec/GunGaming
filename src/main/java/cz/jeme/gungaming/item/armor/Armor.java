package cz.jeme.gungaming.item.armor;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.loot.SingleLoot;
import cz.jeme.gungaming.util.Lores;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Equippable;
import io.papermc.paper.registry.keys.SoundEventKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
public abstract class Armor extends CustomItem implements SingleLoot {

    protected final double armor = provideArmor();
    protected final double toughness = provideToughness();
    protected final EquipmentSlot slot = provideSlot();
    protected final Key armorKey = provideArmorKey();
    protected final int durability = provideDurability();
    protected final Key equipSound = provideEquipSound();

    protected Armor() {
        addTags("armor");
        item.editMeta(meta -> {
                    meta.addAttributeModifier(
                            Attribute.ARMOR,
                            new AttributeModifier(
                                    GunGaming.key(key.value() + "_armor"),
                                    armor,
                                    AttributeModifier.Operation.ADD_NUMBER,
                                    slot.getGroup()
                            )
                    );
                    meta.addAttributeModifier(
                            Attribute.ARMOR_TOUGHNESS,
                            new AttributeModifier(
                                    GunGaming.key(key.value() + "_armor_toughness"),
                                    toughness,
                                    AttributeModifier.Operation.ADD_NUMBER,
                                    slot.getGroup()
                            )
                    );
                }
        );

        item.setData(
                DataComponentTypes.EQUIPPABLE,
                Equippable.equippable(slot)
                        .assetId(armorKey)
                        .equipSound(equipSound)
                        .build()
        );

        item.setData(DataComponentTypes.DAMAGE, 0);
        item.setData(DataComponentTypes.MAX_DAMAGE, durability);
        item.setData(DataComponentTypes.MAX_STACK_SIZE, 1);
    }

    protected double provideArmor() {
        return 0;
    }

    protected double provideToughness() {
        return 0;
    }

    @Override
    protected final Material provideMaterial() {
        return Material.POPPED_CHORUS_FRUIT;
    }

    protected abstract Key provideArmorKey();

    protected abstract int provideDurability();

    protected abstract EquipmentSlot provideSlot();

    protected Key provideEquipSound() {
        return SoundEventKeys.ITEM_ARMOR_EQUIP_GENERIC;
    }

    public final double armor() {
        return armor;
    }

    public final double toughness() {
        return toughness;
    }

    public final EquipmentSlot slot() {
        return slot;
    }

    public final Key armorKey() {
        return armorKey;
    }

    public final int durability() {
        return durability;
    }

    public Key equipSound() {
        return equipSound;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 1;
    }

    protected final Sound heldSound = Sound.sound(GunGaming.key("item.armor.held"), Sound.Source.PLAYER, 1.9F, 1);

    @Override
    public Sound heldSound(final ItemStack item) {
        return heldSound;
    }

    @Override
    protected List<String> update(final ItemStack item) {
        return List.of(
                Lores.loreStat("Armor", Lores.STATS_FORMATTER.format(armor)),
                Lores.loreStat("Toughness", Lores.STATS_FORMATTER.format(toughness))
        );
    }
}
