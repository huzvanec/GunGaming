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
    public Float recoilPercentage = null;
    public Float inaccuracyPercentage = null;

    public Stock() {
        setup();

        assert recoilPercentage != null : "Recoil percentage is null!";
        assert inaccuracyPercentage != null : "Inaccuracy percentage is null!";

        ItemMeta scopeMeta = placeHolder.getItemMeta();
        scopeMeta.displayName(Messages.from("<!italic><gray>Stock</gray></!italic>"));
        scopeMeta.setCustomModelData(3);
        placeHolder.setItemMeta(scopeMeta);
    }

    @Override
    protected Material getMaterial() {
        return Material.IRON_AXE;
    }

    @Override
    public int getSlotId() {
        return 3;
    }

    @Override
    public Namespaces getNbt() {
        return Namespaces.GUN_STOCK;
    }

    public static void update(ItemStack item) {
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

    @Override
    protected Class<? extends Attachment> getGroupClass() {
        return Stock.class;
    }
}
