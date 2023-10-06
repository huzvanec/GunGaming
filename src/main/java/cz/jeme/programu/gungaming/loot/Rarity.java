package cz.jeme.programu.gungaming.loot;

import cz.jeme.programu.gungaming.util.Message;
import org.jetbrains.annotations.NotNull;

public enum Rarity {
    COMMON(14, "<white>"),
    UNCOMMON(12, "<green>"),
    RARE(8, "<blue>"),
    EPIC(3, "<dark_purple>"),
    LEGENDARY(1, "<rainbow>", "<#FF0000><obf>#</obf></#FF0000> ", " <#FF0000><obf>#</obf></#FF0000>"),
    UNOBTAINABLE(0, "<dark_red>");

    private final int chance;
    private final @NotNull String name;
    private final @NotNull String color;

    Rarity(int chance, @NotNull String color) {
        this.chance = chance;
        this.color = color;
        name = color + this;
    }

    Rarity(int chance, @NotNull String color, @NotNull String prefix, @NotNull String suffix) {
        this.chance = chance;
        this.color = color;
        name = prefix + color + this + Message.escape(color) + suffix;
    }

    Rarity(int chance, @NotNull String color, @NotNull String prefix) {
        this(chance, color, prefix, "");
    }

    @Override
    public @NotNull String toString() {
        return name().replace('_', ' ');
    }

    public int getChance() {
        return chance;
    }

    public @NotNull String getColor() {
        return color;
    }

    public @NotNull String getName() {
        return name;
    }
}