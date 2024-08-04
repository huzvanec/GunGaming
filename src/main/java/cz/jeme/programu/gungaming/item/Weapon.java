package cz.jeme.programu.gungaming.item;

public abstract class Weapon extends CustomItem {
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
