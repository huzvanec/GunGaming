package cz.jeme.programu.gungaming.loot;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.item.gun.Gun;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Crate {
    WOODEN_CRATE(
            Collections.emptyMap(),
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
                    Rarity.LEGENDARY, 8),
            Map.of(
                    Gun.class, 2,
                    Attachment.class, 3
            ),
            40,
            Material.GOLD_BLOCK
    );

    private final @NotNull Map<Rarity, Integer> chances;
    public final @NotNull Map<Class<? extends CustomItem>, Integer> limits;
    public final float percentage;
    public final @NotNull Material material;

    Crate(@NotNull Map<Rarity, Integer> chances, @NotNull Map<Class<? extends CustomItem>, Integer> limits, float percentage, @NotNull Material material) {
        for (Integer chance : chances.values()) {
            assert chance != null : "Chance is null in " + this + "!";
            assert chance >= 0 : "Chance is invalid in " + this + "!";
        }

        Map<Rarity, Integer> tempChances = new HashMap<>(chances);
        Arrays.stream(Rarity.values())
                .filter(rarity -> !tempChances.containsKey(rarity))
                .forEach(rarity -> tempChances.put(rarity, rarity.chance));

        assert percentage >= 0 && percentage <= 100 : "Invalid percentage in " + this + "!";

        this.chances = Collections.unmodifiableMap(tempChances);
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
