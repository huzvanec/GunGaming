package cz.jeme.programu.gungaming.item.attachment.stock;

import cz.jeme.programu.gungaming.item.attachment.ModifiersInfo;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class PlasticStock extends Stock {
    @Override
    protected void setup() {
        material = Material.GOLDEN_AXE;
        name = "Plastic Stock";
        info = "Great weapon stability";
        rarity = Rarity.EPIC;
        minLoot = 1;
        maxLoot = 1;
        recoilPercentage = 50f;
        inaccuracyPercentage = 40f;
        modifiersInfo = new ModifiersInfo();
        modifiersInfo.addBuff("-50% recoil");
        modifiersInfo.addBuff("+60% accuracy");
    }
}
