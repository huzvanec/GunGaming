package cz.jeme.programu.gungaming.item.attachment.scope;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class HighScope extends Scope {
    @Override
    protected void setup() {
        material = Material.DIAMOND_PICKAXE;
        name = "High Scope";
        info = "10× scope";
        rarity = Rarity.LEGENDARY;
        minLoot = 1;
        maxLoot = 1;
        scope = 10D;
    }
}
