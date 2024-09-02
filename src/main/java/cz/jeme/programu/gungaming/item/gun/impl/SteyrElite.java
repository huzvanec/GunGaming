package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.FiveFiveSixMillimeter;
import cz.jeme.programu.gungaming.item.attachment.disable.GripDisabled;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class SteyrElite extends Gun implements GripDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 5;
    }

    @Override
    protected int provideShootCooldown() {
        return 30;
    }

    @Override
    protected int provideReloadDuration() {
        return 52;
    }

    @Override
    protected double provideDamage() {
        return 16;
    }

    @Override
    protected double provideBulletVelocity() {
        return 13;
    }

    @Override
    protected double provideRecoil() {
        return .3;
    }

    @Override
    protected double provideInaccuracy() {
        return .2;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return FiveFiveSixMillimeter.class;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "lightweight sniper rifle";
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "steyr_elite";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Steyr Elite");
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 22;
    }
}
