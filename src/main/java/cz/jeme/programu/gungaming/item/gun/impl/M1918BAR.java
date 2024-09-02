package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.SevenSixTwoMillimeter;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class M1918BAR extends Gun {
    @Override
    protected int provideMaxAmmo() {
        return 20;
    }

    @Override
    protected int provideShootCooldown() {
        return 4;
    }

    @Override
    protected int provideReloadDuration() {
        return 51;
    }

    @Override
    protected double provideDamage() {
        return 2.8;
    }

    @Override
    protected double provideBulletVelocity() {
        return 6;
    }

    @Override
    protected double provideRecoil() {
        return .06;
    }

    @Override
    protected double provideInaccuracy() {
        return 1.2;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return SevenSixTwoMillimeter.class;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "A strong LMG with high dps";
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "m1918_bar";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("M1918 BAR");
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 23;
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
