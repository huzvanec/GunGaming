package cz.jeme.programu.gungaming.item.attachment.stock;

import cz.jeme.programu.gungaming.item.attachment.ModifiersInfo;
import cz.jeme.programu.gungaming.loot.Rarity;
import org.bukkit.Material;

public class MetalStock extends Stock {
    @Override
    protected void setup() {
        material = Material.IRON_AXE;
        name = "Metal Stock";
        info = "Awesome weapon handling";
        rarity = Rarity.LEGENDARY;
        minLoot = 1;
        maxLoot = 1;
        recoilPercentage = 20f;
        inaccuracyPercentage = 10f;
        modifiersInfo = new ModifiersInfo();
        modifiersInfo.addBuff("-80% recoil");
        modifiersInfo.addBuff("+90% accuracy");
    }
}
