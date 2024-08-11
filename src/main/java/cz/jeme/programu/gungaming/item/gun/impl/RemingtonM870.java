package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.TwelveGauge;
import cz.jeme.programu.gungaming.item.attachment.disable.MagazineDisabled;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class RemingtonM870 extends Gun implements MagazineDisabled {

    @Override
    protected int provideMaxAmmo() {
        return 5;
    }

    @Override
    protected int provideShootCooldown() {
        return 25;
    }

    @Override
    protected int provideReloadDuration() {
        return 13;
    }

    @Override
    protected double provideDamage() {
        return 2.3;
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
        return 4.5;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return TwelveGauge.class;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "remington_m870";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Remington M870");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Good shotgun for close-range";
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

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 6;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }
}
