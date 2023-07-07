package cz.jeme.programu.gungaming.loot;

import cz.jeme.programu.gungaming.util.Messages;
import net.kyori.adventure.text.Component;

public enum Rarity {
    COMMON(12, "<white>"),
    UNCOMMON(10, "<green>"),
    RARE(5, "<aqua>"),
    EPIC(2, "<dark_purple>"),
    LEGENDARY(1, "<rainbow>", "<#FF0000><obf>#</obf></#FF0000> ", " <#FF0000><obf>#</obf></#FF0000>");

    public final int chance;
    public final String name;
    public final String color;
    public final Component component;

    Rarity(int chance, String color) {
        this.chance = chance;
        this.color = color;
        name = color + this;
        component = Messages.from(name);
    }

    Rarity(int chance, String color, String prefix, String suffix) {
        this.chance = chance;
        this.color = color;
        name = prefix + color + this + Messages.getEscapeTag(color) + suffix;
        component = Messages.from(name);
    }

    Rarity(int chance, String color, String prefix) {
        this(chance, color, prefix, "");
    }

    @Override
    public String toString() {
        return name().replace('_', ' ');
    }
}