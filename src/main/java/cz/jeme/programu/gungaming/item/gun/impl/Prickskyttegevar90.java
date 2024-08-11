package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.ThreeZeroEightSubsonicWinchester;
import cz.jeme.programu.gungaming.item.attachment.disable.GripDisabled;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

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
        return 25;
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
        return .6;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return ThreeZeroEightSubsonicWinchester.class;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "The best sniper rifle in the game";
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "prickskyttegevar_90";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Prickskyttegevär 90");
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 9;
    }
}
