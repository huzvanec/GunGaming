package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.SevenSixTwoMillimeter;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class SCARH extends Gun {
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
        return 54;
    }

    @Override
    protected double provideDamage() {
        return 3.8;
    }

    @Override
    protected double provideBulletVelocity() {
        return 8;
    }

    @Override
    protected double provideRecoil() {
        return .03;
    }

    @Override
    protected double provideInaccuracy() {
        return .5;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return SevenSixTwoMillimeter.class;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "extremely accurate assault rifle with high damage";
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "scar-h";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("SCAR-H");
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
        return 16;
    }
}
