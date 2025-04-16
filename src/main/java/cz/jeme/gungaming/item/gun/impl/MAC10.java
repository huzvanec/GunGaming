package cz.jeme.gungaming.item.gun.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.item.ammo.impl.NineMillimeter;
import cz.jeme.gungaming.item.attachment.disable.GripDisabled;
import cz.jeme.gungaming.item.attachment.disable.ScopeDisabled;
import cz.jeme.gungaming.item.gun.Gun;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MAC10 extends Gun implements GripDisabled, ScopeDisabled {

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
    protected Class<? extends Ammo> provideAmmoType() {
        return NineMillimeter.class;
    }

    @Override
    protected String provideDescription() {
        return "high fire rate SMG with low accuracy";
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "mac-10";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.RARE;
    }

    @Override
    protected Component provideName() {
        return Component.text("MAC-10");
    }

    @Override
    protected int provideBulletsPerShot() {
        return 4;
    }

}
