package cz.jeme.programu.gungaming.item.attachment.magazine;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class HugeMagazine extends Magazine {
    @Override
    public int getCustomModelData() {
        return 3;
    }

    @Override
    public @NotNull String getName() {
        return "Huge Magazine";
    }

    @Override
    public @NotNull String getInfo() {
        return "Huge extended storage for ammo";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @NotNull Set<String> getBuffs() {
        return Set.of("+50% ammo");
    }

    @Override
    protected @NotNull Set<String> getNerfs() {
        return Set.of("-50% reload speed");
    }

    @Override
    public float getMagazineSizePercentage() {
        return 1.5f;
    }
}