package cz.jeme.programu.gungaming.item.attachment.stock;

import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.Namespaces;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Stock extends Attachment {
    public static final ItemStack PLACE_HOLDER = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);

    static {
        ItemMeta scopeMeta = PLACE_HOLDER.getItemMeta();
        scopeMeta.displayName(Messages.from("<!italic>Stock</!italic>"));
        scopeMeta.setCustomModelData(1);
        PLACE_HOLDER.setItemMeta(scopeMeta);
    }

    {
        id = 3;
        placeHolder = PLACE_HOLDER;
        nbt = Namespaces.GUN_STOCK;
    }
}
