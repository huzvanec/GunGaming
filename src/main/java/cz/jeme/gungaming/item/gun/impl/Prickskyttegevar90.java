package cz.jeme.gungaming.item.gun.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.item.ammo.impl.ThreeZeroEightSubsonicWinchester;
import cz.jeme.gungaming.item.attachment.disable.GripDisabled;
import cz.jeme.gungaming.item.gun.Gun;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Prickskyttegevar90 extends Gun implements GripDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 10;
    }

    @Override
    protected int provideShootCooldown() {
        return 32;
    }

    @Override
    protected int provideReloadDuration() {
        return 60;
    }

    @Override
    protected double provideDamage() {
        return 24;
    }

    @Override
    protected double provideBulletVelocity() {
        return 6;
    }

    @Override
    protected double provideRecoil() {
        return .2;
    }

    @Override
    protected double provideInaccuracy() {
        return .3;
    }

    @Override
    protected Class<? extends Ammo> provideAmmoType() {
        return ThreeZeroEightSubsonicWinchester.class;
    }

    @Override
    protected String provideDescription() {
        return "The best sniper rifle in the game";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "prickskyttegevar_90";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected Component provideName() {
        return Component.text("Prickskyttegevär 90");
    }

}
