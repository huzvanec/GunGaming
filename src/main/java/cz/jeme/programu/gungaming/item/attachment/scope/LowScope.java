package cz.jeme.programu.gungaming.item.attachment.scope;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public final class LowScope extends Scope {

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public @NotNull String getName() {
        return "Low Scope";
    }

    @Override
    public @NotNull String getInfo() {
        return "Basic scope for close-range";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    protected @NotNull Set<String> getBuffs() {
        return Set.of("+2× scope");
    }

    @Override
    protected @NotNull Set<String> getNerfs() {
        return Collections.emptySet();
    }

    @Override
    public double getScopeMultiplier() {
        return 2D;
    }
}
