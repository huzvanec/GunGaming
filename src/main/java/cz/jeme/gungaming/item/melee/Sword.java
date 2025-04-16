package cz.jeme.gungaming.item.melee;

import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class Sword extends Melee {
    @Override
    protected double provideAttackSpeedBonus() {
        return -2.4;
    }
}
