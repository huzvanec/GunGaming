package cz.jeme.gungaming.item.gun.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.item.ammo.impl.TwelveGauge;
import cz.jeme.gungaming.item.attachment.disable.MagazineDisabled;
import cz.jeme.gungaming.item.gun.Gun;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class RemingtonM1100 extends Gun implements MagazineDisabled {

    @Override
    protected int provideMaxAmmo() {
        return 4;
    }

    @Override
    protected int provideShootCooldown() {
        return 10;
    }

    @Override
    protected int provideReloadDuration() {
        return 13;
    }

    @Override
    protected double provideDamage() {
        return 1.1;
    }

    @Override
    protected double provideBulletVelocity() {
        return 5;
    }

    @Override
    protected double provideRecoil() {
        return .02;
    }

    @Override
    protected double provideInaccuracy() {
        return 9;
    }

    @Override
    protected Class<? extends Ammo> provideAmmoType() {
        return TwelveGauge.class;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "remington_m1100";
    }

    @Override
    protected Component provideName() {
        return Component.text("Remington M1100");
    }

    @Override
    protected String provideDescription() {
        return "OK shotgun with big spread";
    }

    @Override
    protected int provideBulletsPerShot() {
        return 18;
    }

    @Override
    protected int provideBulletCooldown() {
        return 0;
    }

    @Override
    protected boolean provideMagazineless() {
        return true;
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.UNCOMMON;
    }
}
