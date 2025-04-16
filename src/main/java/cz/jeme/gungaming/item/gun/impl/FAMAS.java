package cz.jeme.gungaming.item.gun.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.item.ammo.impl.FiveFiveSixMillimeter;
import cz.jeme.gungaming.item.attachment.disable.StockDisabled;
import cz.jeme.gungaming.item.gun.Gun;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class FAMAS extends Gun implements StockDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 25;
    }

    @Override
    protected int provideShootCooldown() {
        return 8;
    }

    @Override
    protected int provideReloadDuration() {
        return 45;
    }

    @Override
    protected double provideDamage() {
        return 3;
    }

    @Override
    protected double provideBulletVelocity() {
        return 7;
    }

    @Override
    protected double provideRecoil() {
        return .05;
    }

    @Override
    protected double provideInaccuracy() {
        return .5;
    }

    @Override
    protected Class<? extends Ammo> provideAmmoType() {
        return FiveFiveSixMillimeter.class;
    }

    @Override
    protected String provideDescription() {
        return "very accurate 3-round SMG with high fire rate";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "famas";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected Component provideName() {
        return Component.text("FAMAS");
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
