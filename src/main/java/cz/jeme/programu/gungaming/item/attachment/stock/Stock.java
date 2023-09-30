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
    public @NotNull Float recoilPercentage;
    public @NotNull Float inaccuracyPercentage;
    public static final @NotNull ItemStack SHOTGUN_STOCK_PLACEHOLDER = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);

    static {
        ItemMeta meta = SHOTGUN_STOCK_PLACEHOLDER.getItemMeta();
        meta.displayName(Message.from("<!italic><gray>Stock</gray></!italic>"));
        meta.lore(List.of(Message.from("<!italic><red>Stocks only have quarter effects on shotguns!</red></!italic>")));
        meta.setCustomModelData(6);
        SHOTGUN_STOCK_PLACEHOLDER.setItemMeta(meta);
    }

    public Stock() {
        setup();

        assert recoilPercentage != null : "Recoil percentage is null!";
        assert inaccuracyPercentage != null : "Inaccuracy percentage is null!";

        ItemMeta scopeMeta = placeHolder.getItemMeta();
        scopeMeta.displayName(Message.from("<!italic><gray>Stock</gray></!italic>"));
        scopeMeta.setCustomModelData(3);
        placeHolder.setItemMeta(scopeMeta);
    }

    @Override
    protected @NotNull Material getMaterial() {
        return Material.IRON_AXE;
    }

    @Override
    public int getSlotId() {
        return 3;
    }

    @Override
    public @NotNull Namespace getNbt() {
        return Namespace.GUN_STOCK;
    }

    public static void update(ItemStack item) {
        String stockName = Namespace.GUN_STOCK.get(item);
        assert stockName != null : "Stock name is null!";
        Gun gun = Guns.getGun(item);
        if (stockName.isEmpty()) {
            Namespace.GUN_RECOIL.set(item, gun.recoil);
            Namespace.GUN_INACCURACY.set(item, gun.inaccuracy);
            return;
        }
        Stock stock = (Stock) Attachments.getAttachment(stockName);
        assert stock != null : "Stock is null!";


        float newRecoil;
        float newInaccuracy;

        if (gun.ammoType.equals(TwelveGauge.class)) {
            newRecoil = gun.recoil * ((100 + stock.recoilPercentage) / 400f);
            newInaccuracy = gun.inaccuracy * ((100 + stock.inaccuracyPercentage) / 400f);
        } else {
            newRecoil = gun.recoil * (stock.recoilPercentage / 100f);
            newInaccuracy = gun.inaccuracy * (stock.inaccuracyPercentage / 100f);
        }

        Namespace.GUN_RECOIL.set(item, newRecoil);
        Namespace.GUN_INACCURACY.set(item, newInaccuracy);
    }

    @Override
    protected @NotNull Class<? extends Attachment> getGroupClass() {
        return Stock.class;
    }

    @Override
    public @NotNull ItemStack getPlaceHolder(@NotNull Gun gun) {
        if (gun.ammoType.equals(TwelveGauge.class)) {
            return SHOTGUN_STOCK_PLACEHOLDER;
        } else {
            return super.getPlaceHolder(gun);
        }
    }
}
