package cz.jeme.gungaming.item.melee;

import cz.jeme.gungaming.CustomElement;
import cz.jeme.gungaming.GlobalEventHandler;
import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.item.Weapon;
import cz.jeme.gungaming.util.Lores;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public abstract class Melee extends Weapon {
    protected final double damageBonus = provideDamageBonus();
    protected final double knockbackBonus = provideKnockbackBonus();
    protected final double attackSpeedBonus = provideAttackSpeedBonus();

    protected Melee() {
        item.editMeta(meta -> {
            meta.addAttributeModifier(
                    Attribute.ATTACK_DAMAGE,
                    new AttributeModifier(
                            GunGaming.key(key.value() + "_attack_damage"),
                            damageBonus,
                            AttributeModifier.Operation.ADD_NUMBER,
                            EquipmentSlotGroup.MAINHAND
                    )
            );
            meta.addAttributeModifier(
                    Attribute.ATTACK_KNOCKBACK,
                    new AttributeModifier(
                            GunGaming.key(key.value() + "_attack_knockback"),
                            knockbackBonus,
                            AttributeModifier.Operation.ADD_NUMBER,
                            EquipmentSlotGroup.MAINHAND
                    )
            );
            meta.addAttributeModifier(
                    Attribute.ATTACK_SPEED,
                    new AttributeModifier(
                            GunGaming.key(key.value() + "_attack_speed"),
                            attackSpeedBonus,
                            AttributeModifier.Operation.ADD_NUMBER,
                            EquipmentSlotGroup.MAINHAND
                    )
            );
        });
        addTags("melee");
    }

    @Override
    protected List<String> update(final ItemStack item) {
        final List<String> lore = new ArrayList<>();
        lore.add(Lores.loreStat("Damage", Lores.STATS_FORMATTER.format(damageBonus)));
        lore.add(Lores.loreStat("Attack Speed", Lores.STATS_FORMATTER.format(attackSpeedBonus + 4.1)));
        return lore;
    }

    protected double provideDamageBonus() {
        return 0;
    }

    protected double provideKnockbackBonus() {
        return 0;
    }

    protected double provideAttackSpeedBonus() {
        return 0;
    }

    public final double damage() {
        return damageBonus;
    }

    public final double knockback() {
        return knockbackBonus;
    }

    public final double attackSpeed() {
        return attackSpeedBonus;
    }

    protected void onHit(final EntityDamageEvent event, final ItemStack item) {
        GlobalEventHandler.resetNoDamageTicks(event.getEntity());
    }

    @Override
    protected final String provideType() {
        return "melee weapon";
    }

    public static Melee of(final String keyStr) {
        return CustomElement.of(keyStr, Melee.class);
    }

    public static Melee of(final ItemStack item) {
        return CustomItem.of(item, Melee.class);
    }

    public static boolean is(final String keyStr) {
        return CustomElement.is(keyStr, Melee.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Melee.class);
    }
}
