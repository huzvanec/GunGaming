package cz.jeme.programu.gungaming.item.gun;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.SevenSixTwoMm;
import cz.jeme.programu.gungaming.item.attachment.NoMagazine;
import cz.jeme.programu.gungaming.item.attachment.NoScope;
import cz.jeme.programu.gungaming.item.attachment.NoStock;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

public final class M134Minigun extends Gun implements NoMagazine, NoStock, NoScope {
    @Override
    public int getCustomModelData() {
        return 5;
    }

    @Override
    public @NotNull String getName() {
        return "M134 Minigun";
    }

    @Override
    public @NotNull String getInfo() {
        return "Legendary gatling-like rotary minigun";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public int getShootCooldown() {
        return 200;
    }

    @Override
    public int getReloadCooldown() {
        return 4900;
    }

    @Override
    public double getDamage() {
        return 2.5D;
    }

    @Override
    public float getVelocity() {
        return 40f;
    }

    @Override
    public int getMaxAmmo() {
        return 200;
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
        return 1.3f;
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