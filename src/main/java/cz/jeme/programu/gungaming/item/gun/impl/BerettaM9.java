package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.NineMillimeter;
import cz.jeme.programu.gungaming.item.attachment.disable.GripDisabled;
import cz.jeme.programu.gungaming.item.attachment.disable.StockDisabled;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class BerettaM9 extends Gun implements GripDisabled, StockDisabled {
    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "beretta_m9";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Beretta M9");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Lousy pistol";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.COMMON;
    }

    @Override
    protected int provideMaxAmmo() {
        return 15;
    }

    @Override
    protected int provideShootCooldown() {
        return 8;
    }

    @Override
    protected int provideReloadDuration() {
        return 31;
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
        return .1;
    }

    @Override
    protected double provideInaccuracy() {
        return 1.2;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return NineMillimeter.class;
    }
}
