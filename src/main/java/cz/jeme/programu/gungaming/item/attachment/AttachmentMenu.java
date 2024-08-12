package cz.jeme.programu.gungaming.item.attachment;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.data.Data;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.attachment.disable.*;
import cz.jeme.programu.gungaming.item.attachment.impl.Silencer;
import cz.jeme.programu.gungaming.item.gun.Gun;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AttachmentMenu {
    private static final @NotNull Sound APPLY_SOUND = Sound.sound(GunGaming.namespaced("item.attachment.apply"), Sound.Source.PLAYER, 1, 1);
    private static final @NotNull Sound REMOVE_SOUND = Sound.sound(GunGaming.namespaced("item.attachment.remove"), Sound.Source.PLAYER, 1, 1);

    private final @NotNull HumanEntity player;
    private final @NotNull ItemStack gunItem;
    private @NotNull ItemStack gunItemBackup;
    private final @NotNull Gun gun;
    private final @NotNull Inventory inventory;


    public AttachmentMenu(final @NotNull HumanEntity player, final @NotNull ItemStack gunItem) {
        this.player = player;
        this.gunItem = gunItem;
        gunItemBackup = gunItem.clone();
        gun = Gun.of(gunItem);
        inventory = Bukkit.createInventory(
                player,
                InventoryType.HOPPER,
                gun.strippedName().append(Component.text(" attachments"))
        );

        fill();

        player.openInventory(inventory);
        player.playSound(gun.heldSound(gunItem), player);
    }

    private void fill() {
        final ItemStack silencer = gun instanceof SilencerDisabled
                ? PlaceholderHelper.disabled(Silencer.class)
                : Silencer.GUN_SILENCER_KEY_DATA.read(gunItem)
                .map(key -> CustomItem.of(key).item())
                .orElse(Silencer.placeholder(gunItem));
        final ItemStack grip = gun instanceof GripDisabled
                ? PlaceholderHelper.disabled(Grip.class)
                : Grip.GUN_GRIP_KEY_DATA.read(gunItem)
                .map(key -> CustomItem.of(key).item())
                .orElse(Grip.placeholder(gunItem));
        final ItemStack scope = gun instanceof ScopeDisabled
                ? PlaceholderHelper.disabled(Scope.class)
                : Scope.GUN_SCOPE_KEY_DATA.read(gunItem)
                .map(key -> CustomItem.of(key).item())
                .orElse(Scope.placeholder(gunItem));
        final ItemStack magazine = gun instanceof MagazineDisabled
                ? PlaceholderHelper.disabled(Magazine.class)
                : Magazine.GUN_MAGAZINE_KEY_DATA.read(gunItem)
                .map(key -> CustomItem.of(key).item())
                .orElse(Magazine.placeholder(gunItem));
        final ItemStack stock = gun instanceof StockDisabled
                ? PlaceholderHelper.disabled(Stock.class)
                : Stock.GUN_STOCK_KEY_DATA.read(gunItem)
                .map(key -> CustomItem.of(key).item())
                .orElse(Stock.placeholder(gunItem));
        inventory.setItem(0, silencer);
        inventory.setItem(1, grip);
        inventory.setItem(2, scope);
        inventory.setItem(3, magazine);
        inventory.setItem(4, stock);
    }

    void inventoryClick(final @NotNull InventoryClickEvent event) {
        if (inventory == event.getClickedInventory()) {
            menuClick(event);
            return;
        }
        if (event.getClickedInventory() != null) {
            playerInventoryClick(event);
            return;
        }
    }

    private void menuClick(final @NotNull InventoryClickEvent event) {
        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;
        final int slot = event.getSlot();
        final PlayerInventory playerInventory = event.getWhoClicked().getInventory();
        final boolean placeholder = PlaceholderHelper.PLACEHOLDER_DATA.read(clickedItem)
                .orElse(false);
        final boolean disabled = PlaceholderHelper.DISABLED_DATA.read(clickedItem)
                .orElse(false);
        switch (event.getClick()) {
            case LEFT, RIGHT -> {
                final ItemStack heldItem = event.getCursor();
                if (!disabled && checkAttachmentSlot(heldItem, slot)) {
                    // valid placing of an attachment
                    if (placeholder) event.setCurrentItem(null); // remove the placeholder in the slot
                    else remove(slot, clickedItem, false); // remove the item in the slot
                    apply(slot, heldItem);
                    return;
                }
                if (placeholder || disabled) {
                    event.setCancelled(true);
                    return;
                }
                if (heldItem.isEmpty()) {
                    remove(slot, clickedItem, true);
                    return;
                }
                event.setCancelled(true);
            }
            case SHIFT_LEFT, SHIFT_RIGHT -> {
                if (placeholder || disabled || playerInventory.firstEmpty() == -1) {
                    event.setCancelled(true);
                    return;
                }
                remove(slot, clickedItem, true);
            }
            case NUMBER_KEY -> {
                event.setCancelled(true);
                final int hotbarSlot = event.getHotbarButton();
                final ItemStack hotbarItem = playerInventory.getItem(hotbarSlot);
                if (!disabled && checkAttachmentSlot(hotbarItem, slot)) {
                    ItemStack clickedItemBackup = null;
                    if (!placeholder) {
                        clickedItemBackup = clickedItem.clone();
                        remove(slot, clickedItem, false);
                    }
                    inventory.setItem(slot, hotbarItem);
                    playerInventory.setItem(hotbarSlot, clickedItemBackup);
                    apply(slot, hotbarItem);
                    return;
                }
                final boolean hotbarEmpty = hotbarItem == null || hotbarItem.isEmpty();
                if (placeholder || disabled || !hotbarEmpty) return;
                playerInventory.setItem(hotbarSlot, clickedItem);
                remove(slot, clickedItem, true);
            }
            default -> event.setCancelled(true);
        }
    }

    private void playerInventoryClick(final @NotNull InventoryClickEvent event) {
        assert event.getClickedInventory() != null;
        final ItemStack clickedItem = event.getCurrentItem();
        switch (event.getClick()) {
            case SHIFT_LEFT, SHIFT_RIGHT -> {
                if (!Attachment.is(clickedItem)) return;
                final int slot = attachmentToSlot(Attachment.of(clickedItem));
                final ItemStack slotItem = inventory.getItem(slot);
                if (!PlaceholderHelper.PLACEHOLDER_DATA.read(slotItem).orElse(false)) return;
                inventory.setItem(slot, clickedItem);
                apply(slot, clickedItem);
                event.getClickedInventory().setItem(event.getSlot(), ItemStack.empty());
            }
        }
    }

    private void apply(final int slot, final @NotNull ItemStack attachmentItem) {
        final Data<String, String> keyData = slotToData(slot);
        final Attachment attachment = Attachment.of(attachmentItem);
//        player.sendMessage(Component.text("Applied: ").append(attachment.name()));
        keyData.write(gunItem, attachment.key().asString());
        gunItemBackup = gunItem.clone();
        attachment.apply(player, gunItem);
        gun.updateItem(gunItem);
        player.playSound(APPLY_SOUND, player);
    }

    private void remove(final int slot, final @NotNull ItemStack attachmentItem, final boolean setPlaceholder) {
        slotToData(slot).delete(gunItem);
        gunItemBackup = gunItem.clone();
        final Attachment attachment = Attachment.of(attachmentItem);
//        player.sendMessage(Component.text("Removed: ").append(attachment.name()));
        attachment.remove(player, gunItem);
        gun.updateItem(gunItem);
        player.playSound(REMOVE_SOUND, player);
        if (!setPlaceholder) return;
        Bukkit.getScheduler().runTaskLater(
                GunGaming.plugin(),
                () -> inventory.setItem(slot, slotToPlaceholder(slot)),
                0L
        );
    }

    void playerDropItem(final @NotNull PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().equals(gunItemBackup))
            player.closeInventory();
    }

    private static @NotNull Class<? extends Attachment> slotToClass(final int slot) {
        return switch (slot) {
            case 0 -> Silencer.class;
            case 1 -> Grip.class;
            case 2 -> Scope.class;
            case 3 -> Magazine.class;
            case 4 -> Stock.class;
            default -> throw new IllegalArgumentException("Invalid slot: " + slot);
        };
    }

    private @NotNull ItemStack slotToPlaceholder(final int slot) {
        return switch (slot) {
            case 0 -> Silencer.placeholder(gunItem);
            case 1 -> Grip.placeholder(gunItem);
            case 2 -> Scope.placeholder(gunItem);
            case 3 -> Magazine.placeholder(gunItem);
            case 4 -> Stock.placeholder(gunItem);
            default -> throw new IllegalArgumentException("Invalid slot: " + slot);
        };
    }

    private @NotNull Data<String, String> slotToData(final int slot) {
        return switch (slot) {
            case 0 -> Silencer.GUN_SILENCER_KEY_DATA;
            case 1 -> Grip.GUN_GRIP_KEY_DATA;
            case 2 -> Scope.GUN_SCOPE_KEY_DATA;
            case 3 -> Magazine.GUN_MAGAZINE_KEY_DATA;
            case 4 -> Stock.GUN_STOCK_KEY_DATA;
            default -> throw new IllegalArgumentException("Invalid slot: " + slot);
        };
    }

    private int attachmentToSlot(final @NotNull Attachment attachment) {
        return switch (attachment) {
            case final Silencer ignored -> 0;
            case final Grip ignored -> 1;
            case final Scope ignored -> 2;
            case final Magazine ignored -> 3;
            case final Stock ignored -> 4;
            default ->
                    throw new IllegalArgumentException("Attachment does not fall under any category: " + attachment.getClass().getCanonicalName());
        };
    }

    private boolean checkAttachmentSlot(final @Nullable ItemStack item, final int slot) {
        return CustomItem.is(item, slotToClass(slot));
    }

    public @NotNull Inventory inventory() {
        return inventory;
    }
}
