package cz.jeme.programu.gungaming.eventhandler.interaction;

import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.eventhandler.PlayerItemConsumeHandler;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.item.throwable.Throwable;
import cz.jeme.programu.gungaming.loot.Crate;
import cz.jeme.programu.gungaming.manager.CooldownManager;
import cz.jeme.programu.gungaming.util.Inventories;
import cz.jeme.programu.gungaming.util.Materials;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.Sounds;
import cz.jeme.programu.gungaming.util.item.Attachments;
import cz.jeme.programu.gungaming.util.item.Consumables;
import cz.jeme.programu.gungaming.util.item.Guns;
import cz.jeme.programu.gungaming.util.item.Throwables;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RightClickHandler {

    private final CooldownManager cooldownManager = CooldownManager.getInstance();
    private final PlayerItemConsumeHandler consumeHandler;

    public RightClickHandler(PlayerItemConsumeHandler consumeHandler) {
        this.consumeHandler = consumeHandler;
    }

    public void air(PlayerInteractEvent event) {
        activateInteract(event);
    }

    public void block(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            throw new NullPointerException("Clicked block is null!");
        }
        Material material = clickedBlock.getType();
        switch (material) {
            case BARREL -> {
                crate(clickedBlock, Crate.WOODEN_CRATE);
                return;
            }
            case CHEST -> {
                crate(clickedBlock, Crate.GOLDEN_CRATE);
                return;
            }
        }
        if (!Materials.hasRightClick(material)) {
            activateInteract(event);
            ItemStack item = event.getItem();
            if (Guns.isGun(item) || Attachments.isAttachment(item)) {
                event.setCancelled(true);
            }
        }
    }

    private void crate(Block block, Crate crate) {

    }

    private void activateInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if (Guns.isGun(heldItem)) {
            shoot(event, player, heldItem);
            return;
        }
        if (Throwables.isThrowable(heldItem)) {
            doThrow(event, player, heldItem);
            return;
        }
        if (Consumables.isConsumable(heldItem)) {
            consumeHandler.onStartConsume(event);
            return;
        }
    }

    private void shoot(PlayerInteractEvent event, Player player, ItemStack heldItem) {
        event.setCancelled(true);

        Gun gun = Guns.getGun(heldItem);
        if (player.getCooldown(heldItem.getType()) != 0) {
            return;
        }

        Integer heldAmmo = Namespace.GUN_AMMO_CURRENT.get(heldItem);
        assert heldAmmo != null : "Held gun current ammo is null!";
        boolean isCreative = player.getGameMode() == GameMode.CREATIVE;
        if (heldAmmo == 0 && !isCreative) {
            if (Inventories.getItemCount(player.getInventory(), gun.ammo.item) == 0) {
                player.sendActionBar(Messages.from("<red>Out of ammo!</red>"));
                player.playSound(Sounds.getSound("gun.out_of_ammo", 2.5f));
            } else {
                player.sendActionBar(Messages.from("<red>Press F to reload!</red>"));
                player.playSound(Sounds.getSound("gun.reload_required", 2.5f));
            }
            return;
        }

        cooldownManager.setCooldown(player, heldItem.getType(), gun.shootCooldown);
        gun.shoot(event, heldItem);
    }

    private void doThrow(PlayerInteractEvent event, Player player, ItemStack heldItem) {
        event.setCancelled(true);

        if (player.getCooldown(heldItem.getType()) != 0) return;

        Throwable throwable = Throwables.getThrowable(heldItem);

        cooldownManager.setCooldown(player, heldItem.getType(), throwable.throwCooldown);
        throwable.doThrow(event, heldItem);
    }
}
