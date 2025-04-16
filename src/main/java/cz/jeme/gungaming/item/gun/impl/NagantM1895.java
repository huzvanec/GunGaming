package cz.jeme.gungaming.item.gun.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.item.ammo.impl.SevenSixTwoMillimeter;
import cz.jeme.gungaming.item.attachment.disable.GripDisabled;
import cz.jeme.gungaming.item.attachment.disable.MagazineDisabled;
import cz.jeme.gungaming.item.attachment.disable.StockDisabled;
import cz.jeme.gungaming.item.gun.Gun;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class NagantM1895 extends Gun implements GripDisabled, MagazineDisabled, StockDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 7;
    }

    @Override
    protected int provideShootCooldown() {
        return 10;
    }

    @Override
    protected int provideReloadDuration() {
        return 37;
    }

    @Override
    protected double provideDamage() {
        return 5;
    }

    @Override
    protected double provideBulletVelocity() {
        return 6;
    }

    @Override
    protected double provideRecoil() {
        return .18;
    }

    @Override
    protected double provideInaccuracy() {
        return .8;
    }

    @Override
    protected Class<? extends Ammo> provideAmmoType() {
        return SevenSixTwoMillimeter.class;
    }

    @Override
    protected String provideDescription() {
        return "Basic revolver";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "nagant_m1895";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.COMMON;
    }

    @Override
    protected Component provideName() {
        return Component.text("Nagant M1895");
    }

}
