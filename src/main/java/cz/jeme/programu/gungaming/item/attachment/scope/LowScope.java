package cz.jeme.programu.gungaming.item.attachment.scope;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

public class LowScope extends Scope {
    @Override
    protected void setup() {
        customModelData = 1;
        name = "Low Scope";
        info = "A basic scope for close-range";
        rarity = Rarity.RARE;
        scope = 2D;
    }

    @Override
    protected @NotNull String[] getBuffs() {
        return new String[]{"2× scope"};
    }

    @Override
    protected @NotNull String[] getNerfs() {
        return new String[0];
    }
}
