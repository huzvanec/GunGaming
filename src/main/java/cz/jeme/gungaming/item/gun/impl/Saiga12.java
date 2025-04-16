package cz.jeme.gungaming.item.gun.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.item.ammo.impl.TwelveGauge;
import cz.jeme.gungaming.item.gun.Gun;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Saiga12 extends Gun {
    @Override
    protected int provideMaxAmmo() {
        return 5;
    }

    @Override
    protected int provideShootCooldown() {
        return 8;
    }

    @Override
    protected int provideReloadDuration() {
        return 46;
    }

    @Override
    protected double provideDamage() {
        return 2;
    }

    @Override
    protected double provideBulletVelocity() {
        return 5;
    }

    @Override
    protected double provideRecoil() {
        return .04;
    }

    @Override
    protected double provideInaccuracy() {
        return 5;
    }

    @Override
    protected Class<? extends Ammo> provideAmmoType() {
        return TwelveGauge.class;
    }

    @Override
    protected String provideDescription() {
        return "A fully automatic shotgun";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "saiga-12";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected Component provideName() {
        return Component.text("Saiga-12");
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
