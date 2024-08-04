package cz.jeme.programu.gungaming.item.melee;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GlobalEventHandler;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.Weapon;
import cz.jeme.programu.gungaming.util.Lores;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class Melee extends Weapon {
    protected final double damage = provideDamage();
    protected final double knockback = provideKnockback();
    protected final double attackSpeed = provideAttackSpeed();

    protected Melee() {
        item.editMeta(meta -> {
            meta.addAttributeModifier(
                    Attribute.GENERIC_ATTACK_DAMAGE,
                    new AttributeModifier(
                            GunGaming.namespaced("attack_damage"),
                            damage - 1,
                            AttributeModifier.Operation.ADD_NUMBER
                    )
            );
            meta.addAttributeModifier(
                    Attribute.GENERIC_ATTACK_KNOCKBACK,
                    new AttributeModifier(
                            GunGaming.namespaced("attack_knockback"),
                            knockback,
                            AttributeModifier.Operation.ADD_NUMBER
                    )
            );
            meta.addAttributeModifier(
                    Attribute.GENERIC_ATTACK_SPEED,
                    new AttributeModifier(
                            GunGaming.namespaced("attack_speed"),
                            attackSpeed,
                            AttributeModifier.Operation.ADD_NUMBER
                    )
            );
        });
        addTags("melee");
    }

    @Override
    protected @NotNull List<String> update(final @NotNull ItemStack item) {
        final List<String> lore = new ArrayList<>();
        lore.add(Lores.loreStat("Damage", Lores.STATS_FORMATTER.format(damage)));
        lore.add(Lores.loreStat("Attack Speed", Lores.STATS_FORMATTER.format(attackSpeed + 4.1)));
        return lore;
    }

    protected abstract double provideDamage();

    protected double provideKnockback() {
        return 0;
    }

    protected double provideAttackSpeed() {
        return -2.5;
    }

    public final double damage() {
        return damage;
    }

    public final double knockback() {
        return knockback;
    }

    public final double attackSpeed() {
        return attackSpeed;
    }

    protected void onHit(final @NotNull EntityDamageEvent event, final @NotNull ItemStack item) {
        GlobalEventHandler.resetNoDamageTicks(event.getEntity());
    }


    public static @NotNull Melee of(final @NotNull String keyStr) {
        return CustomElement.of(keyStr, Melee.class);
    }

    public static @NotNull Melee of(final @NotNull ItemStack item) {
        return CustomItem.of(item, Melee.class);
    }

    public static boolean is(final @NotNull String keyStr) {
        return CustomElement.is(keyStr, Melee.class);
    }

    public static boolean is(final @Nullable ItemStack item) {
        return CustomItem.is(item, Melee.class);
    }
}
