package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.SevenSixTwoMm;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

public final class AK47 extends Gun {
    @Override
    public int getCustomModelData() {
        return 3;
    }

    @Override
    public @NotNull String getName() {
        return "AK-47";
    }

    @Override
    public @NotNull String getInfo() {
        return "Good assault rifle";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public int getShootCooldown() {
        return 200;
    }

    @Override
    public int getReloadCooldown() {
        return 2250;
    }

    @Override
    public double getDamage() {
        return 1.2D;
    }

    @Override
    public float getVelocity() {
        return 40f;
    }

    @Override
    public int getMaxAmmo() {
        return 30;
    }

    @Override
    public @NotNull Class<? extends Ammo> getAmmoType() {
        return SevenSixTwoMm.class;
    }

    @Override
    public float getRecoil() {
        return 0.05f;
    }

    @Override
    public float getInaccuracy() {
        return 1.7f;
    }

    @Override
    public int getBulletsPerShot() {
        return 2;
    }

    @Override
    public int getBulletCooldown() {
        return 2;
    }
}