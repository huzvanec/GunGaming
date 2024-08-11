package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.SevenSixTwoMillimeter;
import cz.jeme.programu.gungaming.item.attachment.disable.GripDisabled;
import cz.jeme.programu.gungaming.item.attachment.disable.MagazineDisabled;
import cz.jeme.programu.gungaming.item.attachment.disable.StockDisabled;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class NagantM1895 extends Gun implements GripDisabled, MagazineDisabled, StockDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 7;
    }

    @Override
    protected int provideShootCooldown() {
        return 10;
    }

    @Override
    protected int provideReloadDuration() {
        return 37;
    }

    @Override
    protected double provideDamage() {
        return 4.5;
    }

    @Override
    protected double provideBulletVelocity() {
        return 40;
    }

    @Override
    protected double provideRecoil() {
        return .18;
    }

    @Override
    protected double provideInaccuracy() {
        return .9;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return SevenSixTwoMillimeter.class;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Basic revolver";
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "nagant_m1895";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Nagant M1895");
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 2;
    }
}
