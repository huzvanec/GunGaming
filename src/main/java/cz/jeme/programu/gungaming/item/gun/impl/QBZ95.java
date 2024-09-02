package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.FiveFiveSixMillimeter;
import cz.jeme.programu.gungaming.item.attachment.disable.GripDisabled;
import cz.jeme.programu.gungaming.item.attachment.disable.StockDisabled;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class QBZ95 extends Gun implements GripDisabled, StockDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 75;
    }

    @Override
    protected int provideShootCooldown() {
        return 4;
    }

    @Override
    protected int provideReloadDuration() {
        return 75;
    }

    @Override
    protected double provideDamage() {
        return 3.3;
    }

    @Override
    protected double provideBulletVelocity() {
        return 6;
    }

    @Override
    protected double provideRecoil() {
        return .045;
    }

    @Override
    protected double provideInaccuracy() {
        return .5;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return FiveFiveSixMillimeter.class;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "high dps LMG with a large magazine capacity";
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "qbz-95";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("QBZ-95");
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
        return 26;
    }
}
