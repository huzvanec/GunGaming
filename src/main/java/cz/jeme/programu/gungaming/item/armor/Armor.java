package cz.jeme.programu.gungaming.item.armor;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.loot.SingleLoot;
import cz.jeme.programu.gungaming.util.Lores;
import net.kyori.adventure.sound.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public abstract class Armor extends CustomItem implements SingleLoot {

    protected final double armor = provideArmor();
    protected final double toughness = provideToughness();
    protected final @NotNull EquipmentSlotGroup slot = provideSlot();

    protected Armor() {
        addTags("armor");
        item.editMeta(meta -> {
                    meta.addAttributeModifier(
                            Attribute.GENERIC_ARMOR,
                            new AttributeModifier(
                                    GunGaming.namespaced(key.value() + "_armor"),
                                    armor,
                                    AttributeModifier.Operation.ADD_NUMBER,
                                    slot
                            )
                    );
                    meta.addAttributeModifier(
                            Attribute.GENERIC_ARMOR_TOUGHNESS,
                            new AttributeModifier(
                                    GunGaming.namespaced(key.value() + "_armor_toughness"),
                                    toughness,
                                    AttributeModifier.Operation.ADD_NUMBER,
                                    slot
                            )
                    );
                }
        );
    }

    protected abstract double provideArmor();

    protected abstract double provideToughness();

    protected abstract @NotNull EquipmentSlotGroup provideSlot();

    public final double armor() {
        return armor;
    }

    public final double toughness() {
        return toughness;
    }

    public final @NotNull EquipmentSlotGroup slot() {
        return slot;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 1;
    }

    protected final @NotNull Sound heldSound = Sound.sound(GunGaming.namespaced("item.armor.held"), Sound.Source.PLAYER, 1.9F, 1);

    @Override
    public @NotNull Sound heldSound(final @NotNull ItemStack item) {
        return heldSound;
    }

    @Override
    protected @NotNull List<String> update(final @NotNull ItemStack item) {
        return List.of(
                Lores.loreStat("Armor", Lores.STATS_FORMATTER.format(armor)),
                Lores.loreStat("Toughness", Lores.STATS_FORMATTER.format(toughness))
        );
    }
}
