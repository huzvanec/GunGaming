package cz.jeme.programu.gungaming.loot;

import cz.jeme.programu.gungaming.util.Messages;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public enum Rarity {
    COMMON(12, "<white>"),
    UNCOMMON(10, "<green>"),
    RARE(5, "<aqua>"),
    EPIC(2, "<dark_purple>"),
    LEGENDARY(1, "<rainbow>", "<#FF0000><obf>#</obf></#FF0000> ", " <#FF0000><obf>#</obf></#FF0000>"),
    UNOBTAINABLE(0, "<dark_red>");

    public final int chance;
    public final @NotNull String name;
    public final @NotNull String color;
    public final @NotNull Component component;

    Rarity(int chance, @NotNull String color) {
        this.chance = chance;
        this.color = color;
        name = color + this;
        component = Messages.from(name);
    }

    Rarity(int chance, @NotNull String color, @NotNull String prefix, @NotNull String suffix) {
        this.chance = chance;
        this.color = color;
        name = prefix + color + this + Messages.getEscapeTag(color) + suffix;
        component = Messages.from(name);
    }

    Rarity(int chance, @NotNull String color, @NotNull String prefix) {
        this(chance, color, prefix, "");
    }

    @Override
    public @NotNull String toString() {
        return name().replace('_', ' ');
    }
}