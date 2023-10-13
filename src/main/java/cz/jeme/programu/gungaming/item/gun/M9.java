package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.NineMm;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

public final class M9 extends Gun {
    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public @NotNull String getName() {
        return "M9";
    }

    @Override
    public @NotNull String getInfo() {
        return "Basic pistol";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    public int getShootCooldown() {
        return 400;
    }

    @Override
    public int getReloadCooldown() {
        return 1550;
    }

    @Override
    public double getDamage() {
        return 3D;
    }

    @Override
    public float getVelocity() {
        return 40f;
    }

    @Override
    public int getMaxAmmo() {
        return 15;
    }

    @Override
    public @NotNull Class<? extends Ammo> getAmmoType() {
        return NineMm.class;
    }

    @Override
    public float getRecoil() {
        return 0.1f;
    }

    @Override
    public float getInaccuracy() {
        return 1.2f;
    }
}