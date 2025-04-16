package cz.jeme.gungaming.item.gun.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.item.ammo.impl.SevenSixTwoMillimeter;
import cz.jeme.gungaming.item.gun.Gun;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DragunovSVU extends Gun {
    @Override
    protected int provideMaxAmmo() {
        return 10;
    }

    @Override
    protected int provideShootCooldown() {
        return 10;
    }

    @Override
    protected int provideReloadDuration() {
        return 41;
    }

    @Override
    protected double provideDamage() {
        return 10.5;
    }

    @Override
    protected double provideBulletVelocity() {
        return 12;
    }

    @Override
    protected double provideRecoil() {
        return .22;
    }

    @Override
    protected double provideInaccuracy() {
        return .3;
    }

    @Override
    protected Class<? extends Ammo> provideAmmoType() {
        return SevenSixTwoMillimeter.class;
    }

    @Override
    protected String provideDescription() {
        return "AK-47 but it's a sniper rifle";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "dragunov_svu";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected Component provideName() {
        return Component.text("Dragunov SVU");
    }

}
