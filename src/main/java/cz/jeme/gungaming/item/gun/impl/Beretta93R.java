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
public class Beretta93R extends Gun implements GripDisabled, StockDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 20;
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
        return 2;
    }

    @Override
    protected double provideBulletVelocity() {
        return 5;
    }

    @Override
    protected double provideRecoil() {
        return .05;
    }

    @Override
    protected double provideInaccuracy() {
        return .9;
    }

    @Override
    protected Class<? extends Ammo> provideAmmoType() {
        return NineMillimeter.class;
    }

    @Override
    protected String provideDescription() {
        return "basic 3-round burst pistol";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "beretta_93r";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    protected Component provideName() {
        return Component.text("Beretta 93R");
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
