package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.NineMillimeter;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class MP5 extends Gun {
    @Override
    protected int provideMaxAmmo() {
        return 30;
    }

    @Override
    protected int provideShootCooldown() {
        return 4;
    }

    @Override
    protected int provideReloadDuration() {
        return 36;
    }

    @Override
    protected double provideDamage() {
        return 1.6;
    }

    @Override
    protected double provideBulletVelocity() {
        return 6;
    }

    @Override
    protected double provideRecoil() {
        return .04;
    }

    @Override
    protected double provideInaccuracy() {
        return 1.1;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return NineMillimeter.class;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "decent accurate SMG";
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "mp5";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("MP5");
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
        return 10;
    }
}
