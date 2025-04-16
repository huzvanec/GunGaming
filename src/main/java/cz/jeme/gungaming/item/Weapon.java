package cz.jeme.gungaming.item;

import cz.jeme.gungaming.loot.SingleLoot;
import org.jspecify.annotations.NullMarked;

@NullMarked
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
