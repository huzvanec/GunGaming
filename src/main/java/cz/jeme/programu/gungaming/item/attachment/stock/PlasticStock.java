package cz.jeme.programu.gungaming.item.attachment.stock;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public final class PlasticStock extends Stock {
    @Override
    public int getCustomModelData() {
        return 2;
    }

    @Override
    public @NotNull String getName() {
        return "Plastic Stock";
    }

    @Override
    public @NotNull String getInfo() {
        return "Great weapon stability";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @NotNull Set<String> getBuffs() {
        return Set.of(
                "-50% recoil",
                "+60% accuracy"
        );
    }

    @Override
    protected @NotNull Set<String> getNerfs() {
        return Collections.emptySet();
    }

    @Override
    public float getRecoilPercentage() {
        return 0.5f;
    }

    @Override
    public float getInaccuracyPercentage() {
        return 0.4f;
    }

    @Override
    public @NotNull String getStabilityName() {
        return "Great";
    }
}
