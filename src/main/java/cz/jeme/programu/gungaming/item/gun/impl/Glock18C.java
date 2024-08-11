package cz.jeme.programu.gungaming.item.gun.impl;

import cz.jeme.programu.gungaming.item.ammo.Ammo;
import cz.jeme.programu.gungaming.item.ammo.impl.NineMillimeter;
import cz.jeme.programu.gungaming.item.attachment.disable.GripDisabled;
import cz.jeme.programu.gungaming.item.attachment.disable.StockDisabled;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class Glock18C extends Gun implements GripDisabled, StockDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 17;
    }

    @Override
    protected int provideShootCooldown() {
        return 4;
    }

    @Override
    protected int provideReloadDuration() {
        return 38;
    }

    @Override
    protected double provideDamage() {
        return 2;
    }

    @Override
    protected double provideBulletVelocity() {
        return 5;
    }

    @Override
    protected double provideRecoil() {
        return .04;
    }

    @Override
    protected double provideInaccuracy() {
        return 5;
    }

    @Override
    protected @NotNull Class<? extends Ammo> provideAmmoType() {
        return NineMillimeter.class;
    }

    @Override
    protected @NotNull String provideDescription() {
        return "fully automatic pistol with big spread";
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "glock_18c";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    protected @NotNull Component provideName() {
        return Component.text("Glock 18C");
    }

    @Override
    protected int provideBulletsPerShot() {
        return 4;
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 17;
    }
}
