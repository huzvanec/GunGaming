package cz.jeme.programu.gungaming.item;

import cz.jeme.programu.gungaming.util.Materials;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ItemEventHandler {
    private ItemEventHandler() {
        throw new AssertionError();
    }

    public static void onPlayerInteract(final @NotNull PlayerInteractEvent event) {
        if (!event.hasItem()) return;
        final ItemStack item = event.getItem();
        assert item != null;
        if (!CustomItem.is(item)) return;
        final CustomItem customItem = CustomItem.of(item);
        switch (event.getAction()) {
            case LEFT_CLICK_AIR -> {
                customItem.onLeftClick(event);
                customItem.onLeftClickAir(event);
            }
            case RIGHT_CLICK_AIR -> {
                customItem.onRightClick(event);
                customItem.onRightClickAir(event);
                customItem.onUse(event);
            }
            case LEFT_CLICK_BLOCK -> {
                customItem.onLeftClick(event);
                customItem.onLeftClickBlock(event);
            }
            case RIGHT_CLICK_BLOCK -> {
                customItem.onRightClick(event);
                customItem.onRightClickBlock(event);

                final Block block = event.getClickedBlock();
                assert block != null;
                if (Materials.hasAction(block.getType()) && !event.getPlayer().isSneaking()) return;

                customItem.onUse(event);
            }
        }


    }

    public static void onPlayerItemHeld(final @NotNull PlayerItemHeldEvent event) {
        final Player player = event.getPlayer();
        final ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        if (!CustomItem.is(newItem)) return;
        final CustomItem customItem = CustomItem.of(newItem);
        player.getWorld().playSound(customItem.heldSound(newItem), player);
    }

    // prevent using custom items in crafting
    public static void onPrepareItemCraft(final @NotNull PrepareItemCraftEvent event) {
        final CraftingInventory inventory = event.getInventory();
        for (final ItemStack item : inventory.getMatrix())
            if (CustomItem.is(item)) {
                inventory.setResult(ItemStack.empty());
                return;
            }
    }
}
