package cz.jeme.programu.gungaming.item;

import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Lores;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.Namespaces;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CustomItem {

    public ItemStack item;

    public Material material = null;

    public String name = null;

    public String info = null;

    public Rarity rarity = null;
    public Integer minLoot = null;
    public Integer maxLoot = null;

    abstract protected void setup();

    public CustomItem() {
        setup();

        assert material != null : "Material not given!";
        assert name != null : "Name not given!";
        assert info != null : "Info not given!";
        assert rarity != null : "No rarity given!";
        assert minLoot != null : "No minimal loot given!";
        assert maxLoot != null : "No maximal loot given!";

        item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        String coloredName = rarity.color + name + Messages.getEscapeTag(rarity.color);
        meta.displayName(Messages.from("<!italic>" + coloredName + "</!italic>"));
        meta.setCustomModelData(1);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        Namespaces.RARITY.set(meta, rarity.name());
        Namespaces.INFO.set(meta, Messages.latin(info));
        Namespaces.GG.set(meta, true);
        item.setItemMeta(meta);
        Lores.update(item);
    }
}
