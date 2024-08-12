package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.TwelveGauge;
import cz.jeme.programu.gungaming.item.attachment.disable.GripDisabled;
import cz.jeme.programu.gungaming.item.attachment.disable.MagazineDisabled;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class BaikalMP220 extends Gun implements GripDisabled, MagazineDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 2;
    }

    @Override
    protected int provideShootCooldown() {
        return 7;
    }

    @Override
    protected int provideReloadDuration() {
        return 50;
    }

    @Override
    protected double provideDamage() {
        return 1.9;
    }

    @Override
    protected double provideBulletVelocity() {
        return 5;
    }

    @Override
    protected double provideRecoil() {
        return .06;
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
        return "two-round shotgun with fast firing";
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "baikal_mp220";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Baikal MP220");
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
        return 19;
    }
}