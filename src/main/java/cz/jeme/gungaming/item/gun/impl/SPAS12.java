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
public class SPAS12 extends Gun implements MagazineDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 9;
    }

    @Override
    protected int provideShootCooldown() {
        return 27;
    }

    @Override
    protected int provideReloadDuration() {
        return 12;
    }

    @Override
    protected double provideDamage() {
        return 2.2;
    }

    @Override
    protected double provideBulletVelocity() {
        return 7;
    }

    @Override
    protected double provideRecoil() {
        return .06;
    }

    @Override
    protected double provideInaccuracy() {
        return 2;
    }

    @Override
    protected Class<? extends Ammo> provideAmmoType() {
        return TwelveGauge.class;
    }

    @Override
    protected String provideDescription() {
        return "very good and accurate shotgun";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "spas-12";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected Component provideName() {
        return Component.text("SPAS-12");
    }

    @Override
    protected int provideBulletsPerShot() {
        return 9;
    }

    @Override
    protected int provideBulletCooldown() {
        return 0;
    }

    @Override
    protected boolean provideMagazineless() {
        return true;
    }

}
