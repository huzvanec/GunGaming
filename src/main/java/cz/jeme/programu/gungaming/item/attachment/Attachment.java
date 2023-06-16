package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.loot.SingleLoot;
import org.bukkit.inventory.ItemStack;


public abstract class Attachment extends CustomItem implements SingleLoot {
    public Integer id = null;
    public ItemStack placeHolder = null;
    public Namespaces nbt = null;
    public ModifiersInfo modifiersInfo = null;

    public Attachment() {
        setup();

        group = Attachment.class;

        assert modifiersInfo != null: "No modifiers info set!";

        Namespaces.ATTACHMENT.set(item, name);
    }
}