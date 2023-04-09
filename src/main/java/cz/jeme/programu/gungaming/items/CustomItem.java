package cz.jeme.programu.gungaming.items;

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

    abstract protected void setup();

    public CustomItem() {
        setup();

        assert material != null : "Material not given!";
        assert name != null : "Name not given!";
        assert loreLine != null : "Lore line not given!";

        item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + name);
        meta.setCustomModelData(1);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GREEN + loreLine);
        meta.setLore(lore);
        item.setItemMeta(meta);

    }
}
