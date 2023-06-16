package cz.jeme.programu.gungaming.item.attachment.stock;

import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.item.Attachments;
import cz.jeme.programu.gungaming.util.item.Guns;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Stock extends Attachment {
    public static final ItemStack PLACE_HOLDER = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
    public Float recoilPercentage = null;
    public Float inaccuracyPercentage = null;


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

    public Stock() {
        setup();

        assert recoilPercentage != null : "Recoil percentage is null!";
        assert inaccuracyPercentage != null : "Inaccuracy percentage is null!";
    }

    public static void updateStock(ItemStack item) {
        String stockName = Namespaces.GUN_STOCK.get(item);
        Gun gun = Guns.getGun(item);
        if (stockName.equals("")) {
            Namespaces.GUN_RECOIL.set(item, gun.recoil);
            Namespaces.GUN_INACCURACY.set(item, gun.inaccuracy);
            return;
        }
        Stock stock = (Stock) Attachments.getAttachment(stockName);

        float newRecoil = gun.recoil * (stock.recoilPercentage / 100f);
        Namespaces.GUN_RECOIL.set(item, newRecoil);

        float newInaccuracy = gun.inaccuracy * (stock.inaccuracyPercentage / 100f);
        Namespaces.GUN_INACCURACY.set(item, newInaccuracy);
    }
}
