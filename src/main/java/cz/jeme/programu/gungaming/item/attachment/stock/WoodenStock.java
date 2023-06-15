package cz.jeme.programu.gungaming.item.attachment.stock;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class WoodenStock extends Stock {
    @Override
    protected void setup() {
        material = Material.IRON_PICKAXE;
        name = "Wooden Stock";
        info = "A great stock";
        rarity = Rarity.RARE;
        minLoot = 1;
        maxLoot = 1;
    }
}
