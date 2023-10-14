package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.SevenSixTwoMm;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

public final class SV98 extends Gun {
    @Override
    public int getCustomModelData() {
        return 8;
    }

    @Override
    public @NotNull String getName() {
        return "SV-98";
    }

    @Override
    public @NotNull String getInfo() {
        return "Powerful sniper rifle";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public int getShootCooldown() {
        return 1400;
    }

    @Override
    public int getReloadCooldown() {
        return 2500;
    }

    @Override
    public double getDamage() {
        return 18D;
    }

    @Override
    public float getVelocity() {
        return 80f;
    }

    @Override
    public int getMaxAmmo() {
        return 10;
    }

    @Override
    public @NotNull Class<? extends Ammo> getAmmoType() {
        return SevenSixTwoMm.class;
    }

    @Override
    public float getRecoil() {
        return 0.333f;
    }

    @Override
    public float getInaccuracy() {
        return 0.4f;
    }
}