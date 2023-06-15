package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.util.Namespaces;
import org.bukkit.inventory.ItemStack;

public abstract class Attachment extends CustomItem {
    public Integer id = null;
    public ItemStack placeHolder = null;
    public Namespaces nbt = null;

    public Attachment() {
        Namespaces.ATTACHMENT.set(item, name);
    }
}