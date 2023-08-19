package cz.jeme.programu.gungaming.item.attachment.scope;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.jetbrains.annotations.NotNull;

public class MediumScope extends Scope {
    @Override
    protected void setup() {
        customModelData = 2;
        name = "Medium Scope";
        info = "A precise scope for mid-range";
        rarity = Rarity.EPIC;
        scope = 5D;
    }

    @Override
    protected @NotNull String[] getBuffs() {
        return new String[]{"5× scope"};
    }

    @Override
    protected @NotNull String[] getNerfs() {
        return new String[0];
    }
}
