package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.ThreeZeroEightSubsonicWinchester;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

public final class Prickskyttegevar90 extends Gun {
    @Override
    public int getCustomModelData() {
        return 9;
    }

    @Override
    public @NotNull String getName() {
        return "Prickskyttegevar 90";
    }

    @Override
    public @NotNull String getDisplayName() {
        return "Prickskyttegevär 90";
    }

    @Override
    public @NotNull String getInfo() {
        return "The best sniper rifle in the game";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public int getShootCooldown() {
        return 1600;
    }

    @Override
    public int getReloadCooldown() {
        return 3000;
    }

    @Override
    public double getDamage() {
        return 30D;
    }

    @Override
    public float getVelocity() {
        return 30f;
    }

    @Override
    public int getMaxAmmo() {
        return 10;
    }

    @Override
    public @NotNull Class<? extends Ammo> getAmmoType() {
        return ThreeZeroEightSubsonicWinchester.class;
    }

    @Override
    public float getRecoil() {
        return 0.2f;
    }

    @Override
    public float getInaccuracy() {
        return 0.6f;
    }
}