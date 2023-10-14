package cz.jeme.programu.gungaming.item.attachment.scope;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public final class MediumScope extends Scope {
    @Override
    public int getCustomModelData() {
        return 2;
    }

    @Override
    public @NotNull String getName() {
        return "Medium Scope";
    }

    @Override
    public @NotNull String getInfo() {
        return "Precise scope for mid-range";
    }

    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected @NotNull Set<String> getBuffs() {
        return Set.of("+5× scope");
    }

    @Override
    protected @NotNull Set<String> getNerfs() {
        return Collections.emptySet();
    }

    @Override
    public double getScopeMultiplier() {
        return 5D;
    }
}
