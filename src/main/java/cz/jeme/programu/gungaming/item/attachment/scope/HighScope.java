package cz.jeme.programu.gungaming.item.attachment.scope;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public final class HighScope extends Scope {
    @Override
    public int getCustomModelData() {
        return 3;
    }

    @Override
    public @NotNull String getName() {
        return "High Scope";
    }

    @Override
    public @NotNull String getInfo() {
        return "Extreme scope for long-range";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @NotNull Set<String> getBuffs() {
        return Set.of("10× scope");
    }

    @Override
    protected @NotNull Set<String> getNerfs() {
        return Collections.emptySet();
    }

    @Override
    public double getScopeMultiplier() {
        return 10D;
    }
}
