package cz.jeme.programu.gungaming.item.attachment.stock;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.ammo.TwelveGauge;
import cz.jeme.programu.gungaming.item.attachment.Attachment;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.util.Message;
import cz.jeme.programu.gungaming.util.registry.Attachments;
import cz.jeme.programu.gungaming.util.registry.Guns;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Stock extends Attachment {
    public abstract float getRecoilPercentage();

    public abstract float getInaccuracyPercentage();

    public static final @NotNull ItemStack SHOTGUN_STOCK_PLACEHOLDER = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);

    static {
        ItemMeta meta = SHOTGUN_STOCK_PLACEHOLDER.getItemMeta();
        meta.displayName(Message.from("<!italic><gray>Stock</gray></!italic>"));
        meta.lore(List.of(
                Message.from("<!italic><red>"
                        + Message.latin("Stocks have only quarter effects on shotguns!")
                        + "</red></!italic>")
        ));
        meta.setCustomModelData(6);
        SHOTGUN_STOCK_PLACEHOLDER.setItemMeta(meta);
    }

    {
        ItemMeta scopeMeta = placeHolder.getItemMeta();
        scopeMeta.displayName(Message.from("<!italic><gray>Stock</gray></!italic>"));
        scopeMeta.setCustomModelData(3);
        placeHolder.setItemMeta(scopeMeta);
    }

    @Override
    public final @NotNull Material getMaterial() {
        return Material.SKULL_BANNER_PATTERN;
    }

    @Override
    public final int getSlotId() {
        return 3;
    }

    @Override
    public final @NotNull Namespace getNamespace() {
        return Namespace.GUN_STOCK;
    }

    public static void update(@NotNull ItemStack item) {
        String stockName = Namespace.GUN_STOCK.get(item);
        assert stockName != null : "Stock name is null!";
        Gun gun = Guns.getGun(item);
        if (stockName.isEmpty()) {
            Namespace.GUN_RECOIL.set(item, gun.getRecoil());
            Namespace.GUN_INACCURACY.set(item, gun.getInaccuracy());
            return;
        }
        Stock stock = (Stock) Attachments.getAttachment(stockName);
        assert stock != null : "Stock is null!";


        float newRecoil;
        float newInaccuracy;

        if (gun.getAmmoType() == TwelveGauge.class) {
            newRecoil = gun.getRecoil() * ((100 + stock.getRecoilPercentage()) / 4f);
            newInaccuracy = gun.getInaccuracy() * ((100 + stock.getInaccuracyPercentage()) / 4f);
        } else {
            newRecoil = gun.getRecoil() * stock.getRecoilPercentage();
            newInaccuracy = gun.getInaccuracy() * stock.getInaccuracyPercentage();
        }

        Namespace.GUN_RECOIL.set(item, newRecoil);
        Namespace.GUN_INACCURACY.set(item, newInaccuracy);
    }

    @Override
    protected final @NotNull Class<? extends Attachment> getAttachmentType() {
        return Stock.class;
    }

    @Override
    public final @NotNull ItemStack getPlaceHolder(@NotNull Gun gun) {
        if (gun.getAmmoType() == TwelveGauge.class) return SHOTGUN_STOCK_PLACEHOLDER;
        return super.getPlaceHolder(gun);
    }
}
