package cz.jeme.gungaming.item.gun.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.item.ammo.impl.SevenSixTwoMillimeter;
import cz.jeme.gungaming.item.attachment.disable.AttachmentsDisabled;
import cz.jeme.gungaming.item.gun.Gun;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class M134Minigun extends Gun implements AttachmentsDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 200;
    }

    @Override
    protected int provideShootCooldown() {
        return 4;
    }

    @Override
    protected int provideReloadDuration() {
        return 98;
    }

    @Override
    protected double provideDamage() {
        return 3;
    }

    @Override
    protected double provideBulletVelocity() {
        return 8;
    }

    @Override
    protected double provideRecoil() {
        return .05;
    }

    @Override
    protected double provideInaccuracy() {
        return 1.3;
    }

    @Override
    protected Class<? extends Ammo> provideAmmoType() {
        return SevenSixTwoMillimeter.class;
    }

    @Override
    protected String provideDescription() {
        return "Gatling-like rotary minigun";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "m134_minigun";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected Component provideName() {
        return Component.text("M134 Minigun");
    }

    @Override
    protected int provideBulletsPerShot() {
        return 2;
    }

    @Override
    protected int provideBulletCooldown() {
        return 2;
    }

}
