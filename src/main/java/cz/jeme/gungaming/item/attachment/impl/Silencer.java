package cz.jeme.gungaming.item.attachment.impl;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.attachment.Attachment;
import cz.jeme.gungaming.item.attachment.PlaceholderHelper;
import cz.jeme.gungaming.item.gun.Gun;
import cz.jeme.gungaming.loot.Rarity;
import cz.jeme.gungaming.persistence.PersistentData;
import cz.jeme.gungaming.util.Components;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class Silencer extends Attachment {
    public static final PersistentData<String, String> GUN_SILENCER_KEY_DATA = PersistentData.ofString(GunGaming.key("gun_silencer_key"));
    private static final ItemStack PLACEHOLDER = PlaceholderHelper.placeholder(
            GunGaming.key("silencer_placeholder"),
            meta -> meta.displayName(Components.of("<!i><gray>Silencer"))
    );

    public static ItemStack placeholder(final ItemStack gunItem) {
        return PLACEHOLDER.clone();
    }

    protected final double volumeMultiplier = provideVolumeMultiplier();
    protected final double damageMultiplier = provideDamageMultiplier();

    protected double provideVolumeMultiplier() {
        return .05;
    }

    protected double provideDamageMultiplier() {
        return .95;
    }

    @Override
    protected List<String> provideBuffs() {
        return List.of(
                "-95% gunshot volume"
        );
    }

    @Override
    protected List<String> provideDebuffs() {
        return List.of(
                "-5% damage"
        );
    }

    public final double volumeMultiplier() {
        return volumeMultiplier;
    }

    public final double damageMultiplier() {
        return damageMultiplier;
    }

    @Override
    public void apply(final HumanEntity player, final ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        Gun.DAMAGE_DATA.write(gunItem, gun.damage() * damageMultiplier);
    }

    @Override
    public void remove(final HumanEntity player, final ItemStack gunItem) {
        final Gun gun = Gun.of(gunItem);
        Gun.DAMAGE_DATA.write(gunItem, gun.damage());
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "silencer";
    }

    @Override
    protected Component provideName() {
        return Component.text("Silencer");
    }

    @Override
    protected String provideDescription() {
        return "Quietens gunfire";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.EPIC;
    }
}
