package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.SevenSixTwoMm;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

public final class NagantM1895 extends Gun {
    @Override
    public int getCustomModelData() {
        return 2;
    }

    @Override
    public @NotNull String getName() {
        return "Nagant M1895";
    }

    @Override
    public @NotNull String getInfo() {
        return "Lousy revolver";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    public int getShootCooldown() {
        return 530;
    }

    @Override
    public int getReloadCooldown() {
        return 1850;
    }

    @Override
    public double getDamage() {
        return 4D;
    }

    @Override
    public float getVelocity() {
        return 40f;
    }

    @Override
    public int getMaxAmmo() {
        return 7;
    }

    @Override
    public @NotNull Class<? extends Ammo> getAmmoType() {
        return SevenSixTwoMm.class;
    }

    @Override
    public float getRecoil() {
        return 0.18f;
    }

    @Override
    public float getInaccuracy() {
        return 0.9f;
    }
}