package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.FiveFiveSixMillimeter;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class M249SAW extends Gun {
    @Override
    protected int provideMaxAmmo() {
        return 100;
    }

    @Override
    protected int provideShootCooldown() {
        return 4;
    }

    @Override
    protected int provideReloadDuration() {
        return 130;
    }

    @Override
    protected double provideDamage() {
        return 3.2;
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
        return 1.2;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return FiveFiveSixMillimeter.class;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "powerful light machine gun with insane magazine capacity";
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "m249_saw";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("M249 SAW");
    }

    @Override
    protected int provideBulletsPerShot() {
        return 2;
    }

    @Override
    protected int provideBulletCooldown() {
        return 2;
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 27;
    }
}
