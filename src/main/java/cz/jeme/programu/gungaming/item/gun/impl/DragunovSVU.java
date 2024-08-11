package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.SevenSixTwoMillimeter;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class DragunovSVU extends Gun {
    @Override
    protected int provideMaxAmmo() {
        return 10;
    }

    @Override
    protected int provideShootCooldown() {
        return 10;
    }

    @Override
    protected int provideReloadDuration() {
        return 41;
    }

    @Override
    protected double provideDamage() {
        return 6;
    }

    @Override
    protected double provideBulletVelocity() {
        return 12;
    }

    @Override
    protected double provideRecoil() {
        return .22;
    }

    @Override
    protected double provideInaccuracy() {
        return .7;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return SevenSixTwoMillimeter.class;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "AK-47 but it's a DMR";
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "dragunov_svu";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Dragunov SVU");
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 7;
    }
}
