package cz.jeme.programu.gungaming.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public abstract class CustomItem {

	public ItemStack item = null;

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
		meta.setDisplayName(ChatColor.GOLD + name);
		meta.setCustomModelData(1);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_GREEN + loreLine);
		meta.setLore(lore);
		item.setItemMeta(meta);

	}
}
