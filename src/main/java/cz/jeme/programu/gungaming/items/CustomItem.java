package cz.jeme.programu.gungaming.items;

import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.utils.LatinUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomItem {

    public ItemStack item;

    public Material material = null;

    public String name = null;

    public String loreLine = null;
    public Rarity rarity = null;

    abstract protected void setup();

    public CustomItem() {
        setup();

        assert material != null : "Material not given!";
        assert name != null : "Name not given!";
        assert loreLine != null : "Lore line not given!";
        assert rarity != null : "No rarity given!";

        item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RESET + name);
        meta.setCustomModelData(1);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        List<String> lore = new ArrayList<>();
        lore.add(rarity.getColor().toString() + ChatColor.BOLD + rarity);
        lore.add(ChatColor.DARK_GREEN.toString() + ChatColor.ITALIC + LatinUtils.toLatin(loreLine));
        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}
