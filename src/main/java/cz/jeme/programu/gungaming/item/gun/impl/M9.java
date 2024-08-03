package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.NineMillimeter;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class M9 extends Gun {
    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "m9";
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("M9");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Basic pistol";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 1;
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.UNCOMMON;
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
        return 40;
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
