package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.NineMillimeter;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class KRISSVector extends Gun {
    @Override
    protected int provideMaxAmmo() {
        return 33;
    }

    @Override
    protected int provideShootCooldown() {
        return 4;
    }

    @Override
    protected int provideReloadDuration() {
        return 30;
    }

    @Override
    protected double provideDamage() {
        return 1.2;
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
        return .7;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return NineMillimeter.class;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Extremely fast and accurate SMG";
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "kriss_vector";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("KRISS Vector");
    }

    @Override
    protected int provideBulletsPerShot() {
        return 4;
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 15;
    }
}
