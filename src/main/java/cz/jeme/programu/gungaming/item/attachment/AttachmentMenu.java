package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.ammo.TwelveGauge;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class AttachmentMenu {

    private static final @NotNull ItemStack EMPTY = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
    private static final @NotNull ItemStack INAPLICABLE = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);

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

    private final @NotNull ItemStack gunItem;
    private final @NotNull Gun gun;
    private final @NotNull Player player;
    private final @NotNull Component title;
    private final @NotNull Inventory inventory;


    public AttachmentMenu(@NotNull InventoryClickEvent event) {
        ItemStack gunItem = event.getCurrentItem();
        assert gunItem != null : "Current item is null!";
        this.gunItem = gunItem;
        gun = Guns.getGun(gunItem);
        player = (Player) event.getWhoClicked();
        title = Messages.from(gun.rarity.color + gun.name + Messages.getEscapeTag(gun.rarity.color) + " attachments");
        inventory = Bukkit.createInventory(player, InventoryType.HOPPER, title);

        inventory.setItem(0, EMPTY);
        inventory.setItem(4, EMPTY);
        read();

        player.openInventory(inventory);
    }

    private @NotNull ItemStack getAttachmentItem(@NotNull String name, @NotNull Class<? extends Attachment> clazz) {
        if (name.isEmpty()) return Attachments.placeHolders.get(clazz);
        Attachment attachment = Attachments.getAttachment(name);
        assert attachment != null : "Attachment is null!";
        return attachment.item;
    }

    private void read() {
        ItemStack scopeItem;
        ItemStack magazineItem;
        ItemStack stockItem;

        if (gun instanceof NoScope) {
            scopeItem = INAPLICABLE;
        } else {
            String scopeName = Namespace.GUN_SCOPE.get(gunItem);
            assert scopeName != null : "Scope name is null!";
            scopeItem = getAttachmentItem(scopeName, Scope.class);
        }
        inventory.setItem(2, scopeItem);

        if (gun instanceof NoMagazine) {
            magazineItem = INAPLICABLE;
        } else {
            String magazineName = Namespace.GUN_MAGAZINE.get(gunItem);
            assert magazineName != null : "Magazine name is null!";
            magazineItem = getAttachmentItem(magazineName, Magazine.class);
        }

        inventory.setItem(1, magazineItem);
        String stockName = Namespace.GUN_STOCK.get(gunItem);
        assert stockName != null : "Stock name is null!";

        if (gun instanceof NoStock) {
            stockItem = INAPLICABLE;
        } else if (gun.ammoType.equals(TwelveGauge.class) && stockName.isEmpty()) {
            stockItem = Stock.SHOTGUN_STOCK_PLACEHOLDER;
        } else {
            stockItem = getAttachmentItem(stockName, Stock.class);
        }
        inventory.setItem(3, stockItem);
    }

    public void click(@NotNull InventoryClickEvent event) {
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

    private void moveToAttachments(@NotNull InventoryClickEvent event, @NotNull ItemStack clickedItem) {
        Attachment attachment = Attachments.getAttachment(clickedItem);
        int index = attachment.getSlotId();

        ItemStack currentItem = inventory.getItem(index);
        assert currentItem != null;

        if (currentItem.equals(INAPLICABLE)) return;

        if (currentItem.equals(attachment.getPlaceHolder(gun))) {
            event.setCurrentItem(null);
        } else {
            event.setCurrentItem(currentItem);
        }

        inventory.setItem(index, clickedItem);
        attachment.getNbt().set(gunItem, attachment.name);
    }

    private void moveToInventory(@NotNull ItemStack clickedItem) {
        Attachment attachment = Attachments.getAttachment(clickedItem);
        List<ItemStack> didntFit = new ArrayList<>(player.getInventory().addItem(clickedItem).values());
        inventory.setItem(attachment.getSlotId(), attachment.getPlaceHolder(gun));
        attachment.getNbt().set(gunItem, "");

        if (attachment instanceof Magazine) { // Check that the gun is not overloaded
            Integer currentAmmo = Namespace.GUN_AMMO_CURRENT.get(gunItem);
            assert currentAmmo != null : "Current ammo is null!";
            int difference = currentAmmo - gun.maxAmmo;
            if (difference > 0) { // It's overloaded, give the ammo back to the player
                Namespace.GUN_AMMO_CURRENT.set(gunItem, gun.maxAmmo);
                ItemStack ammoItem = new ItemStack(gun.ammo.item);
                ammoItem.setAmount(difference);
                didntFit.addAll(player.getInventory().addItem(ammoItem).values());
            }
        }

        // Drop all the items that didn't fit
        // This includes the attachment and the overloaded ammo if it's a magazine
        didntFit.forEach(item -> player.getWorld().dropItem(player.getLocation(), item));
    }


    private void updateGun() {
        String magazineName = Namespace.GUN_MAGAZINE.get(gunItem);
        assert magazineName != null : "Magazine name is null!";
        Gun gun = Guns.getGun(gunItem);
        if (magazineName.isEmpty()) {
            Namespace.GUN_AMMO_MAX.set(gunItem, gun.maxAmmo);
        } else {
            Magazine magazine = (Magazine) Attachments.getAttachment(magazineName);
            assert magazine != null : "Magazine is null!";
            float multiplier = magazine.magazinePercentage / 100f;
            int multipliedMaxAmmo = Math.round(gun.maxAmmo * multiplier);
            Namespace.GUN_AMMO_MAX.set(gunItem, multipliedMaxAmmo);
        }
        Lores.update(gunItem);
        Integer currentAmmo = Namespace.GUN_AMMO_CURRENT.get(gunItem);
        assert currentAmmo != null : "Current ammo is null!";
        Ammos.set(gunItem, currentAmmo);
    }

    public boolean hasOpenInventory(@NotNull InventoryClickEvent event) {
        return event.getClickedInventory() == inventory || event.getInventory() == inventory;
    }
}