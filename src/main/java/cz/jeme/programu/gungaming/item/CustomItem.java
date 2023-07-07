package cz.jeme.programu.gungaming.item;

import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Lores;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.Namespaces;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CustomItem {

    public ItemStack item;

    public Integer customModelData = null;

    public String name = null;

    public String info = null;

    public Rarity rarity = null;

    abstract protected void setup();
    abstract protected Material getMaterial();
    abstract public int getMinLoot();
    abstract public int getMaxLoot();

    public CustomItem() {
        setup();

        assert customModelData != null : "Cusom model data not given!";
        assert name != null : "Name not given!";
        assert info != null : "Info not given!";
        assert rarity != null : "No rarity given!";

        item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        String coloredName = rarity.color + name + Messages.getEscapeTag(rarity.color);
        meta.displayName(Messages.from("<!italic>" + coloredName + "</!italic>"));
        meta.setCustomModelData(customModelData);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        Namespaces.RARITY.set(meta, rarity.name());
        Namespaces.INFO.set(meta, Messages.latin(info));
        Namespaces.GG.set(meta, true);
        item.setItemMeta(meta);
        Lores.update(item);
    }
}
