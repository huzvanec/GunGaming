package cz.jeme.programu.gungaming.item.attachment.stock;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public final class WoodenStock extends Stock {
    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public @NotNull String getName() {
        return "Wooden Stock";
    }

    @Override
    public @NotNull String getInfo() {
        return "Good weapon stability";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @NotNull Set<String> getBuffs() {
        return Set.of(
                "-20% recoil",
                "+30% accuracy"
        );
    }

    @Override
    protected @NotNull Set<String> getNerfs() {
        return Collections.emptySet();
    }

    @Override
    public float getRecoilPercentage() {
        return 0.8f;
    }

    @Override
    public float getInaccuracyPercentage() {
        return 0.7f;
    }

    @Override
    public @NotNull String getStabilityName() {
        return "Good";
    }
}
