package cz.jeme.programu.gungaming.item.attachment.magazine;

import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.Namespaces;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Magazine extends Attachment {
    public static final ItemStack PLACE_HOLDER = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);

    public Float magazinePercentage = null;

    static {
        ItemMeta scopeMeta = PLACE_HOLDER.getItemMeta();
        scopeMeta.displayName(Messages.from("<!italic>Magazine</!italic>"));
        scopeMeta.setCustomModelData(1);
        PLACE_HOLDER.setItemMeta(scopeMeta);
    }

    {
        id = 2;
        placeHolder = PLACE_HOLDER;
        nbt = Namespaces.GUN_MAGAZINE;
    }

    public Magazine() {
        setup();

        assert magazinePercentage != null : "Magazine enlarge percentage is null!";
    }
}
