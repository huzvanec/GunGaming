package cz.jeme.programu.gungaming.item;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.util.Lores;
import cz.jeme.programu.gungaming.util.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class CustomItem {

    public @NotNull ItemStack item;

    public @NotNull Integer customModelData;

    public @NotNull String name;
    public @NotNull String displayName;

    public @NotNull String info;

    public @NotNull Rarity rarity;

    abstract protected void setup();

    abstract protected @NotNull Material getMaterial();

    abstract public int getMinLoot();

    abstract public int getMaxLoot();

    public CustomItem() {
        setup();

        assert customModelData != null : "Cusom model data not given!";
        assert name != null : "Name not given!";
        assert info != null : "Info not given!";
        assert rarity != null : "Rarity not given!";

        if (displayName == null) displayName = name;

        item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        String coloredName = rarity.color + displayName + Messages.getEscapeTag(rarity.color);
        meta.displayName(Messages.from("<!italic>" + coloredName + "</!italic>"));
        meta.setCustomModelData(customModelData);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        Namespace.RARITY.set(meta, rarity.name());
        Namespace.INFO.set(meta, Messages.latin(info));
        Namespace.GG.set(meta, true);
        item.setItemMeta(meta);
        Lores.update(item);
    }
}
