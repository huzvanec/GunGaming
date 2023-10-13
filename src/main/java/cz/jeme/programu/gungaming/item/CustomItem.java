package cz.jeme.programu.gungaming.item;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Lores;
import cz.jeme.programu.gungaming.util.Message;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class CustomItem {

    protected final @NotNull ItemStack item;

    public final @NotNull ItemStack getItem() {
        return item;
    }

    public abstract int getCustomModelData();

    public abstract @NotNull String getName();

    public @NotNull String getDisplayName() {
        return getName();
    }

    public abstract @NotNull String getInfo();

    public abstract @NotNull Rarity getRarity();

    public abstract @NotNull Material getMaterial();

    abstract public int getMinStackLoot();

    abstract public int getMaxStackLoot();

    public CustomItem() {
        item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        String coloredName = getRarity().getColor() + getDisplayName() + Message.escape(getRarity().getColor());
        meta.displayName(Message.from("<!italic>" + coloredName + "</!italic>"));
        meta.setCustomModelData(getCustomModelData());
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        Namespace.RARITY.set(meta, getRarity().name());
        Namespace.INFO.set(meta, Message.latin(getInfo()));
        Namespace.GG.set(meta, true);
        item.setItemMeta(meta);
        Lores.update(item);
    }
}
