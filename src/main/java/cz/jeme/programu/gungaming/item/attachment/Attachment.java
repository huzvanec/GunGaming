package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.item.CustomItem;
import org.bukkit.inventory.ItemStack;


public abstract class Attachment extends CustomItem {
    public Integer id = null;
    public ItemStack placeHolder = null;
    public Namespaces nbt = null;
    public ModifiersInfo modifiersInfo = null;

    public Attachment() {
        setup();
        assert modifiersInfo != null: "No modifiers info set!";

        Namespaces.ATTACHMENT.set(item, name);
    }
}