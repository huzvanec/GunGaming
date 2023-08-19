package cz.jeme.programu.gungaming.loot;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.item.gun.Gun;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

public enum Crate {
    WOODEN_CRATE(
            Map.of(
                    Rarity.COMMON, 12,
                    Rarity.UNCOMMON, 10,
                    Rarity.RARE, 5,
                    Rarity.EPIC, 2,
                    Rarity.LEGENDARY, 1,
                    Rarity.UNOBTAINABLE, 0),
            Map.of(
                    Gun.class, 1,
                    Attachment.class, 2
            ),
            25,
            Material.OAK_PLANKS
    ),
    GOLDEN_CRATE(
            Map.of(
                    Rarity.COMMON, 5,
                    Rarity.UNCOMMON, 5,
                    Rarity.RARE, 5,
                    Rarity.EPIC, 9,
                    Rarity.LEGENDARY, 8,
                    Rarity.UNOBTAINABLE, 0),
            Map.of(
                    Gun.class, 2,
                    Attachment.class, 4
            ),
            40,
            Material.GOLD_BLOCK
    );

    private final @NotNull Map<Rarity, Integer> chances;
    public final @NotNull Map<Class<? extends CustomItem>, Integer> limits;
    public final float percentage;
    public final @NotNull Material material;

    Crate(@NotNull Map<Rarity, Integer> chances, @NotNull Map<Class<? extends CustomItem>, Integer> limits, float percentage, @NotNull Material material) {
        for (Rarity rarity : Rarity.values()) {
            assert chances.containsKey(rarity) : "There is a rarity declaration missing in " + this + "!";
        }
        for (Integer chance : chances.values()) {
            assert chance != null : "Chance is null in " + this + "!";
            assert chance >= 0 : "Chance is invalid in " + this + "!";
        }
        assert percentage >= 0 && percentage <= 100 : "Invalid percentage in " + this + "!";

        this.chances = Collections.unmodifiableMap(chances);
        this.limits = Collections.unmodifiableMap(limits);
        this.percentage = percentage;
        this.material = material;
    }

    Crate(@NotNull Map<Rarity, Integer> chances, float percentage, @NotNull Material material) {
        this(chances, Collections.emptyMap(), percentage, material);
    }

    public int getChance(@NotNull Rarity rarity) {
        return chances.get(rarity);
    }
}
