package cz.jeme.gungaming.item.gun.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.item.ammo.impl.NineMillimeter;
import cz.jeme.gungaming.item.gun.Gun;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class UMP9 extends Gun {
    @Override
    protected int provideMaxAmmo() {
        return 30;
    }

    @Override
    protected int provideShootCooldown() {
        return 12;
    }

    @Override
    protected int provideReloadDuration() {
        return 36;
    }

    @Override
    protected double provideDamage() {
        return 3.2;
    }

    @Override
    protected double provideBulletVelocity() {
        return 6;
    }

    @Override
    protected double provideRecoil() {
        return .05;
    }

    @Override
    protected double provideInaccuracy() {
        return 0.8;
    }

    @Override
    protected Class<? extends Ammo> provideAmmoType() {
        return NineMillimeter.class;
    }

    @Override
    protected String provideDescription() {
        return "3-round burst SMG with great accuracy";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "ump9";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected Component provideName() {
        return Component.text("UMP9");
    }

    @Override
    protected int provideBulletsPerShot() {
        return 3;
    }

    @Override
    protected int provideBulletCooldown() {
        return 2;
    }

}
