package cz.jeme.programu.gungaming.item.attachment.magazine;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class SmallMagazine extends Magazine {
    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public @NotNull String getName() {
        return "Small Magazine";
    }

    @Override
    public @NotNull String getInfo() {
        return "Small extended storage for ammo";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public float getMagazineSizePercentage() {
        return 1.1f;
    }

    @Override
    protected @NotNull Set<String> getBuffs() {
        return Set.of("+10% ammo");
    }

    @Override
    protected @NotNull Set<String> getNerfs() {
        return Set.of("-10% reload speed");
    }
}