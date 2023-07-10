package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.item.attachment.magazine.Magazine;
import cz.jeme.programu.gungaming.item.attachment.scope.Scope;
import cz.jeme.programu.gungaming.item.attachment.stock.Stock;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.util.Lores;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.item.Ammos;
import cz.jeme.programu.gungaming.util.item.Attachments;
import cz.jeme.programu.gungaming.util.item.Guns;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class AttachmentMenu {

    private static final ItemStack EMPTY = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
    private static final ItemStack INAPLICABLE = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);

    static {
        ItemMeta emptyMeta = EMPTY.getItemMeta();
        emptyMeta.displayName(Messages.from("§"));
        emptyMeta.setCustomModelData(4);
        EMPTY.setItemMeta(emptyMeta);

        ItemMeta inaplicableMeta = INAPLICABLE.getItemMeta();
        inaplicableMeta.displayName(Messages.from("<!italic><red>Inaplicable for this weapon</red></!italic>"));
        inaplicableMeta.setCustomModelData(5);
        INAPLICABLE.setItemMeta(inaplicableMeta);
    }

    private final ItemStack gunItem;
    private final Gun gun;
    private final Player player;
    private final Component title;
    private final Inventory inventory;


    public AttachmentMenu(InventoryClickEvent event) {
        gunItem = event.getCurrentItem();
        gun = Guns.getGun(gunItem);
        if (gun == null) throw new NullPointerException("The attachment item is not a gun!");
        player = (Player) event.getWhoClicked();
        title = Messages.from("<dark_aqua>" + gun.name + " attachments</dark_aqua>");
        inventory = Bukkit.createInventory(player, InventoryType.HOPPER, title);

        inventory.setItem(0, EMPTY);
        inventory.setItem(4, EMPTY);
        read();

        player.openInventory(inventory);
    }

    private ItemStack getAttachmentItem(String name, Class<? extends Attachment> clazz) {
        if (name.equals("")) return Attachments.placeHolders.get(clazz);
        return Attachments.getAttachment(name).item;
    }

    private void read() {
        ItemStack scopeItem;
        ItemStack magazineItem;
        ItemStack stockItem;

        if (gun instanceof NoScope) {
            scopeItem = INAPLICABLE;
        } else {
            String scopeName = Namespaces.GUN_SCOPE.get(gunItem);
            scopeItem = getAttachmentItem(scopeName, Scope.class);
        }
        inventory.setItem(2, scopeItem);

        if (gun instanceof NoMagazine) {
            magazineItem = INAPLICABLE;
        } else {
            String magazineName = Namespaces.GUN_MAGAZINE.get(gunItem);
            magazineItem = getAttachmentItem(magazineName, Magazine.class);
        }

        inventory.setItem(1, magazineItem);

        if (gun instanceof NoStock) {
            stockItem = INAPLICABLE;
        } else {
            String stockName = Namespaces.GUN_STOCK.get(gunItem);
            stockItem = getAttachmentItem(stockName, Stock.class);
        }
        inventory.setItem(3, stockItem);
    }

    public void click(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        Inventory clickedInventory = event.getClickedInventory();
        event.setCancelled(true);

        if (!Attachments.isAttachment(clickedItem)) return;
        if (clickedInventory == null) return;

        if (clickedInventory.getType() == InventoryType.PLAYER) {
            moveToAttachments(event, clickedItem);
        } else {
            moveToInventory(clickedItem);
        }
        updateGun();
        Magazine.update(gunItem);
        Stock.update(gunItem);
    }

    private void moveToAttachments(InventoryClickEvent event, ItemStack clickedItem) {
        Attachment attachment = Attachments.getAttachment(clickedItem);
        int index = attachment.getSlotId();

        ItemStack currentItem = inventory.getItem(index);
        assert currentItem != null;

        if (currentItem.equals(INAPLICABLE)) return;

        if (currentItem.equals(attachment.placeHolder)) {
            event.setCurrentItem(null);
        } else {
            event.setCurrentItem(currentItem);
        }

        inventory.setItem(index, clickedItem);
        attachment.getNbt().set(gunItem, attachment.name);
    }

    private void moveToInventory(ItemStack clickedItem) {
        Attachment attachment = Attachments.getAttachment(clickedItem);
        List<ItemStack> didntFit = new ArrayList<>(player.getInventory().addItem(clickedItem).values());
        inventory.setItem(attachment.getSlotId(), attachment.placeHolder);
        attachment.getNbt().set(gunItem, "");

        if (attachment instanceof Magazine) { // Check that the gun is not overloaded
            int difference = (int) Namespaces.GUN_AMMO_CURRENT.get(gunItem) - gun.maxAmmo;
            if (difference > 0) { // It's overloaded, give the ammo back to the player
                Namespaces.GUN_AMMO_CURRENT.set(gunItem, gun.maxAmmo);
                ItemStack ammoItem = new ItemStack(Ammos.getAmmo(gun).item);
                ammoItem.setAmount(difference);
                didntFit.addAll(player.getInventory().addItem(ammoItem).values());
            }
        }

        // Drop all the items that didn't fit
        // This includes the attachment and the overloaded ammo if it's a magazine
        for (ItemStack item : didntFit) {
            player.getWorld().dropItem(player.getLocation(), item);
        }
    }


    private void updateGun() {
        String magazineName = Namespaces.GUN_MAGAZINE.get(gunItem);
        Gun gun = Guns.getGun(gunItem);
        if (magazineName.equals("")) {
            Namespaces.GUN_AMMO_MAX.set(gunItem, gun.maxAmmo);
        } else {
            Magazine magazine = (Magazine) Attachments.getAttachment(magazineName);
            float multiplier = magazine.magazinePercentage / 100f;
            int multipliedMaxAmmo = Math.round(gun.maxAmmo * multiplier);
            Namespaces.GUN_AMMO_MAX.set(gunItem, multipliedMaxAmmo);
        }
        Lores.update(gunItem);
        Ammos.set(gunItem, Namespaces.GUN_AMMO_CURRENT.get(gunItem));
    }

    public boolean hasOpenInventory(InventoryClickEvent event) {
        return event.getClickedInventory() == inventory || event.getInventory() == inventory;
    }
}
