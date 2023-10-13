package cz.jeme.programu.gungaming.item.attachment.magazine;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class BigMagazine extends Magazine {
    @Override
    public int getCustomModelData() {
        return 2;
    }

    @Override
    public @NotNull String getName() {
        return "Big Magazine";
    }

    @Override
    public @NotNull String getInfo() {
        return "Big extended storage for ammo";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @NotNull Set<String> getBuffs() {
        return Set.of("+30% ammo");
    }

    @Override
    protected @NotNull Set<String> getNerfs() {
        return Set.of("-30% reload speed");
    }

    @Override
    public float getMagazineSizePercentage() {
        return 1.3f;
    }
}
