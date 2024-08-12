package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.NineMillimeter;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class UMP9 extends Gun {
    @Override
    protected int provideMaxAmmo() {
        return 30;
    }

    @Override
    protected int provideShootCooldown() {
        return 12;
    }

    @Override
    protected int provideReloadDuration() {
        return 36;
    }

    @Override
    protected double provideDamage() {
        return 3.5;
    }

    @Override
    protected double provideBulletVelocity() {
        return 6;
    }

    @Override
    protected double provideRecoil() {
        return .05;
    }

    @Override
    protected double provideInaccuracy() {
        return 0.8;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return NineMillimeter.class;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "3-round burst SMG with great accuracy";
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "ump9";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("UMP9");
    }

    @Override
    protected int provideBulletsPerShot() {
        return 3;
    }

    @Override
    protected int provideBulletCooldown() {
        return 2;
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 12;
    }
}
