package cz.jeme.programu.gungaming.item.attachment.scope;

import cz.jeme.programu.gungaming.item.attachment.ModifiersInfo;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class HighScope extends Scope {
    @Override
    protected void setup() {
        material = Material.DIAMOND_PICKAXE;
        name = "High Scope";
        info = "An extreme scope for long-range";
        rarity = Rarity.LEGENDARY;
        minLoot = 1;
        maxLoot = 1;
        scope = 10D;
        modifiersInfo = new ModifiersInfo();
        modifiersInfo.addBuff("10× scope");
    }
}
