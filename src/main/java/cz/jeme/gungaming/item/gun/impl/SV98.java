package cz.jeme.gungaming.item.gun.impl;

import cz.jeme.gungaming.item.ammo.Ammo;
import cz.jeme.gungaming.item.ammo.impl.SevenSixTwoMillimeter;
import cz.jeme.gungaming.item.attachment.disable.GripDisabled;
import cz.jeme.gungaming.item.gun.Gun;
import cz.jeme.gungaming.loot.Rarity;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class SV98 extends Gun implements GripDisabled {
    @Override
    protected int provideMaxAmmo() {
        return 10;
    }

    @Override
    protected int provideShootCooldown() {
        return 26;
    }

    @Override
    protected int provideReloadDuration() {
        return 50;
    }

    @Override
    protected double provideDamage() {
        return 21;
    }

    @Override
    protected double provideBulletVelocity() {
        return 15;
    }

    @Override
    protected double provideRecoil() {
        return 0.3;
    }

    @Override
    protected double provideInaccuracy() {
        return 0.1;
    }

    @Override
    protected Class<? extends Ammo> provideAmmoType() {
        return SevenSixTwoMillimeter.class;
    }

    @Override
    protected Component provideName() {
        return Component.text("SV-98");
    }

    @Override
    protected String provideDescription() {
        return "Very powerful sniper rifle";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "sv-98";
    }

}
