package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.TwelveGauge;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class Saiga12 extends Gun {
    @Override
    protected int provideMaxAmmo() {
        return 5;
    }

    @Override
    protected int provideShootCooldown() {
        return 6;
    }

    @Override
    protected int provideReloadDuration() {
        return 46;
    }

    @Override
    protected double provideDamage() {
        return 3;
    }

    @Override
    protected double provideBulletVelocity() {
        return 5;
    }

    @Override
    protected double provideRecoil() {
        return .04;
    }

    @Override
    protected double provideInaccuracy() {
        return 3;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return TwelveGauge.class;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "A fully automatic shotgun";
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "saiga-12";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Saiga-12");
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
    protected @NotNull Integer provideCustomModelData() {
        return 20;
    }
}
