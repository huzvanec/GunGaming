package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.SevenSixTwoMillimeter;
import cz.jeme.programu.gungaming.item.attachment.disable.GripDisabled;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class SV98 extends Gun implements GripDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 10;
    }

    @Override
    protected int provideShootCooldown() {
        return 28;
    }

    @Override
    protected int provideReloadDuration() {
        return 50;
    }

    @Override
    protected double provideDamage() {
        return 19;
    }

    @Override
    protected double provideBulletVelocity() {
        return 15;
    }

    @Override
    protected double provideRecoil() {
        return 0.3;
    }

    @Override
    protected double provideInaccuracy() {
        return 0.4;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return SevenSixTwoMillimeter.class;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("SV-98");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Very powerful sniper rifle";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "sv-98";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 8;
    }
}
