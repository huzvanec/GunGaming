package cz.jeme.gungaming.item.gun.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.item.ammo.impl.FiveZeroActionExpress;
import cz.jeme.gungaming.item.attachment.disable.GripDisabled;
import cz.jeme.gungaming.item.attachment.disable.StockDisabled;
import cz.jeme.gungaming.item.gun.Gun;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DesertEagle extends Gun implements GripDisabled, StockDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 7;
    }

    @Override
    protected int provideShootCooldown() {
        return 7;
    }

    @Override
    protected int provideReloadDuration() {
        return 46;
    }

    @Override
    protected double provideDamage() {
        return 7;
    }

    @Override
    protected double provideBulletVelocity() {
        return 6;
    }

    @Override
    protected double provideRecoil() {
        return .15;
    }

    @Override
    protected double provideInaccuracy() {
        return 1.2;
    }

    @Override
    protected Class<? extends Ammo> provideAmmoType() {
        return FiveZeroActionExpress.class;
    }

    @Override
    protected String provideDescription() {
        return "Pistol with very high DPS";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "desert_eagle";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected Component provideName() {
        return Component.text("Desert Eagle");
    }

}
