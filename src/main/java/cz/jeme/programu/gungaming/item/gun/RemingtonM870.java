package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.TwelveGauge;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

public final class RemingtonM870 extends Gun implements Magazineless {

    @Override
    public int getCustomModelData() {
        return 6;
    }

    @Override
    public @NotNull String getName() {
        return "Remington M870";
    }

    @Override
    public @NotNull String getInfo() {
        return "Good shotgun";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public int getShootCooldown() {
        return 1250;
    }

    @Override
    public int getReloadCooldown() {
        return 660;
    }

    @Override
    public double getDamage() {
        return 2.3D;
    }

    @Override
    public float getVelocity() {
        return 40f;
    }

    @Override
    public int getMaxAmmo() {
        return 5;
    }

    @Override
    public @NotNull Class<? extends Ammo> getAmmoType() {
        return TwelveGauge.class;
    }

    @Override
    public float getRecoil() {
        return 0.05f;
    }

    @Override
    public float getInaccuracy() {
        return 4.5f;
    }

    @Override
    public int getBulletsPerShot() {
        return 9;
    }

    @Override
    public int getBulletCooldown() {
        return 0;
    }
}