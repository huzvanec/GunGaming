package cz.jeme.programu.gungaming.item;

import cz.jeme.programu.gungaming.loot.SingleLoot;

public abstract class Weapon extends CustomItem implements SingleLoot {
    protected Weapon() {
        addTags("weapon");
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 1;
    }
}
