package cz.jeme.programu.gungaming.eventhandler.interaction;

import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Crate;
import cz.jeme.programu.gungaming.manager.CooldownManager;
import cz.jeme.programu.gungaming.util.Inventories;
import cz.jeme.programu.gungaming.util.Materials;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.Sounds;
import cz.jeme.programu.gungaming.util.item.Guns;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RightClickHandler {

    private final CooldownManager cooldownManager;

    public RightClickHandler(CooldownManager cooldownManager) {
        this.cooldownManager = cooldownManager;
    }

    public void air(PlayerInteractEvent event) {
        shoot(event);
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
            shoot(event);
            ItemStack item = event.getItem();
            if (item != null && Namespaces.GG.has(item)) {
                event.setCancelled(true);
            }
        }
    }

    private void crate(Block block, Crate crate) {

    }

    private void shoot(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (!Guns.isGun(heldItem)) return;
        event.setCancelled(true);

        Gun gun = Guns.getGun(heldItem);
        if (player.getCooldown(heldItem.getType()) != 0) {
            return;
        }

        int heldAmmo = Namespaces.GUN_AMMO_CURRENT.get(heldItem);
        boolean isCreative = player.getGameMode() == GameMode.CREATIVE;
        if (heldAmmo == 0 && !isCreative) {
            if (Inventories.getItemCount(player.getInventory(), gun.ammo.item) == 0) {
                player.sendActionBar(Messages.from("<red>Out of ammo!</red>"));
                player.playSound(Sounds.getSound("gun.out_of_ammo", Float.MAX_VALUE));
            } else {
                player.sendActionBar(Messages.from("<red>Press F to reload!</red>"));
                player.playSound(Sounds.getSound("gun.reload_required", Float.MAX_VALUE));
            }
            return;
        }
        gun.shoot(event, heldItem);
        cooldownManager.setCooldown(player, gun.item.getType(), gun.shootCooldown);
    }
}
