package cz.jeme.programu.gungaming.item.attachment.stock;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public final class MetalStock extends Stock {
    @Override
    public int getCustomModelData() {
        return 3;
    }

    @Override
    public @NotNull String getName() {
        return "Metal Stock";
    }

    @Override
    public @NotNull String getInfo() {
        return "Awesome weapon stability";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @NotNull Set<String> getBuffs() {
        return Set.of(
                "-80% recoil",
                "+90% accuracy"
        );
    }

    @Override
    protected @NotNull Set<String> getNerfs() {
        return Collections.emptySet();
    }

    @Override
    public float getRecoilPercentage() {
        return 0.2f;
    }

    @Override
    public float getInaccuracyPercentage() {
        return 0.1f;
    }

    @Override
    public @NotNull String getStabilityName() {
        return "Awesome";
    }
}