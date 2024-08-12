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

public class Beretta93R extends Gun implements GripDisabled, StockDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 20;
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
        return 2;
    }

    @Override
    protected double provideBulletVelocity() {
        return 5;
    }

    @Override
    protected double provideRecoil() {
        return .05;
    }

    @Override
    protected double provideInaccuracy() {
        return .9;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return NineMillimeter.class;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "basic 3-round burst pistol";
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "beretta_93r";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Beretta 93R");
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
        return 21;
    }
}
