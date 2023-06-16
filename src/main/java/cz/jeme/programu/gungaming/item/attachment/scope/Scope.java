package cz.jeme.programu.gungaming.item.attachment.scope;

import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.Namespaces;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Scope extends Attachment {

    public Double scope = null;

    public static final ItemStack PLACE_HOLDER = new ItemStack(Material.RED_STAINED_GLASS_PANE);

    static {
        ItemMeta scopeMeta = PLACE_HOLDER.getItemMeta();
        scopeMeta.displayName(Messages.from("<!italic><gray>Scope</gray></!italic>"));
        scopeMeta.setCustomModelData(1);
        PLACE_HOLDER.setItemMeta(scopeMeta);
    }

    {
        id = 1;
        placeHolder = PLACE_HOLDER;
        nbt = Namespaces.GUN_SCOPE;
    }

    public Scope() {
        setup();

        assert scope != null : "Scope level is null!";
    }
}
