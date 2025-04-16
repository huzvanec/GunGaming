package cz.jeme.gungaming.item.gun.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.item.ammo.impl.NineMillimeter;
import cz.jeme.gungaming.item.attachment.disable.GripDisabled;
import cz.jeme.gungaming.item.attachment.disable.StockDisabled;
import cz.jeme.gungaming.item.gun.Gun;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BerettaM9 extends Gun implements GripDisabled, StockDisabled {
    @Override
    protected @KeyPattern.Value String provideKey() {
        return "beretta_m9";
    }

    @Override
    protected Component provideName() {
        return Component.text("Beretta M9");
    }

    @Override
    protected String provideDescription() {
        return "Lousy pistol";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.COMMON;
    }

    @Override
    protected int provideMaxAmmo() {
        return 15;
    }

    @Override
    protected int provideShootCooldown() {
        return 8;
    }

    @Override
    protected int provideReloadDuration() {
        return 31;
    }

    @Override
    protected double provideDamage() {
        return 4;
    }

    @Override
    protected double provideBulletVelocity() {
        return 5;
    }

    @Override
    protected double provideRecoil() {
        return .1;
    }

    @Override
    protected double provideInaccuracy() {
        return 1.2;
    }

    @Override
    protected Class<? extends Ammo> provideAmmoType() {
        return NineMillimeter.class;
    }
}
