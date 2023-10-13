package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.SevenSixTwoMm;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

public final class DragunovSVU extends Gun {
    @Override
    public int getCustomModelData() {
        return 7;
    }

    @Override
    public @NotNull String getName() {
        return "Dragunov SVU";
    }

    @Override
    public @NotNull String getInfo() {
        return "AK-47 but it's a sniper rifle";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public int getShootCooldown() {
        return 450;
    }

    @Override
    public int getReloadCooldown() {
        return 2050;
    }

    @Override
    public double getDamage() {
        return 6D;
    }

    @Override
    public float getVelocity() {
        return 60f;
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
        return 0.22f;
    }

    @Override
    public float getInaccuracy() {
        return 0.7f;
    }
}