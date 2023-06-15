package cz.jeme.programu.gungaming.item.attachment.scope;

import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class MediumScope extends Scope {
    @Override
    protected void setup() {
        material = Material.GOLDEN_PICKAXE;
        name = "Medium Scope";
        info = "5× scope";
        rarity = Rarity.EPIC;
        minLoot = 1;
        maxLoot = 1;
        scope = 5D;
    }
}
