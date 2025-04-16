package cz.jeme.gungaming.item.gun.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.item.ammo.impl.TwelveGauge;
import cz.jeme.gungaming.item.attachment.disable.GripDisabled;
import cz.jeme.gungaming.item.attachment.disable.MagazineDisabled;
import cz.jeme.gungaming.item.gun.Gun;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BaikalMP220 extends Gun implements GripDisabled, MagazineDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 2;
    }

    @Override
    protected int provideShootCooldown() {
        return 7;
    }

    @Override
    protected int provideReloadDuration() {
        return 50;
    }

    @Override
    protected double provideDamage() {
        return 1.9;
    }

    @Override
    protected double provideBulletVelocity() {
        return 5;
    }

    @Override
    protected double provideRecoil() {
        return .06;
    }

    @Override
    protected double provideInaccuracy() {
        return 4;
    }

    @Override
    protected Class<? extends Ammo> provideAmmoType() {
        return TwelveGauge.class;
    }

    @Override
    protected String provideDescription() {
        return "two-round shotgun with fast firing";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "baikal_mp220";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected Component provideName() {
        return Component.text("Baikal MP220");
    }

    @Override
    protected int provideBulletsPerShot() {
        return 9;
    }

    @Override
    protected int provideBulletCooldown() {
        return 0;
    }

}