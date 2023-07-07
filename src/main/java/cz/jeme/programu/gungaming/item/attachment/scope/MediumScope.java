package cz.jeme.programu.gungaming.item.attachment.scope;

import cz.jeme.programu.gungaming.loot.Rarity;

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
    protected String[] getBuffs() {
        return new String[]{"5× scope"};
    }

    @Override
    protected String[] getNerfs() {
        return new String[0];
    }
}
