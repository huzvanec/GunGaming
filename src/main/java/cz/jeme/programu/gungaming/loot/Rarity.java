package cz.jeme.programu.gungaming.loot;

import net.md_5.bungee.api.ChatColor;

public enum Rarity {
    COMMON(15, ChatColor.WHITE),
    UNCOMMON(10, ChatColor.GREEN),
    RARE(3, ChatColor.AQUA),
    EPIC(2, ChatColor.DARK_PURPLE),
    LEGENDARY(1, ChatColor.GOLD);

    private final int chance;
    private final ChatColor color;

    Rarity(int chance, ChatColor color) {
        this.chance = chance;
        this.color = color;
    }

    public int getChance() {
        return chance;
    }

    public ChatColor getColor() {
        return color;
    }
}
