package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.NineMillimeter;
import cz.jeme.programu.gungaming.item.attachment.disable.ScopeDisabled;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class MAC10 extends Gun implements ScopeDisabled {

    @Override
    protected int provideMaxAmmo() {
        return 32;
    }

    @Override
    protected int provideShootCooldown() {
        return 4;
    }

    @Override
    protected int provideReloadDuration() {
        return 32;
    }

    @Override
    protected double provideDamage() {
        return 1.5;
    }

    @Override
    protected double provideBulletVelocity() {
        return 6;
    }

    @Override
    protected double provideRecoil() {
        return .02;
    }

    @Override
    protected double provideInaccuracy() {
        return 4;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return NineMillimeter.class;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "high fire rate SMG with low accuracy";
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "mac-10";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("MAC-10");
    }

    @Override
    protected int provideBulletsPerShot() {
        return 4;
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 11;
    }
}
