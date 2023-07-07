package cz.jeme.programu.gungaming.item.attachment.scope;

import cz.jeme.programu.gungaming.loot.Rarity;

public class HighScope extends Scope {
    @Override
    protected void setup() {
        customModelData = 3;
        name = "High Scope";
        info = "An extreme scope for long-range";
        rarity = Rarity.LEGENDARY;
        scope = 10D;
    }

    @Override
    protected String[] getBuffs() {
        return new String[]{"10× scope"};
    }

    @Override
    protected String[] getNerfs() {
        return new String[0];
    }
}
