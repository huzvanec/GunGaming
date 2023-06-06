package cz.jeme.programu.gungaming.eventhandler.interaction;

import cz.jeme.programu.gungaming.ArrowVelocityListener;
import cz.jeme.programu.gungaming.items.guns.Gun;
import cz.jeme.programu.gungaming.loot.LootGenerator;
import cz.jeme.programu.gungaming.managers.CooldownManager;
import cz.jeme.programu.gungaming.utils.*;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RightClickHandler {

    private final ArrowVelocityListener arrowVelocityListener;
    private final CooldownManager cooldownManager;

    public RightClickHandler(CooldownManager cooldownManager, ArrowVelocityListener arrowVelocityListener) {
        this.cooldownManager = cooldownManager;
        this.arrowVelocityListener = arrowVelocityListener;
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
        if (material == Material.BARREL) {
            crate(clickedBlock);
            return;
        }
        if (!Arrays.asList(Materials.CONTAINERS).contains(material)) {
            shoot(event);
        }
    }

    private void crate(Block block) {
        Inventory inventory = ((InventoryHolder) block.getState()).getInventory();
        for (ItemStack item : LootGenerator.generateCrate(inventory.getSize(), 30)) {
            blockDrop(block, item);
        }
        block.setType(Material.AIR);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spawnParticle(Particle.SMOKE_LARGE, block.getX() + 0.5f, block.getY(), block.getZ() + 0.5f, 30, 0, 0, 0, 0.1, null);
        }

    }

    private void blockDrop(Block block, ItemStack item) {
        block.getWorld().dropItemNaturally(block.getLocation(), item);
    }

    private void shoot(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (!GunUtils.isGun(heldItem)) {
            return;
        }
        event.setCancelled(true);

        Gun gun = GunUtils.getGun(heldItem);
        if (player.getCooldown(heldItem.getType()) != 0) {
            return;
        }
        int heldAmmo = AmmoLoreUtils.getAmmo(heldItem);
        boolean isCreative = player.getGameMode() == GameMode.CREATIVE;
        if (heldAmmo == 0 && !isCreative) {
            MessageUtils.actionMessage(player, ChatColor.RED + "Reload required");
            return;
        }
        if (!isCreative) {
            AmmoLoreUtils.removeAmmo(heldItem, 1);
        }
        Arrow arrow = gun.shoot(event);
        if (gun.isRocket) {
            arrowVelocityListener.addArrow(arrow);
        }
        cooldownManager.setCooldown(player, gun.item.getType(), gun.shootCooldown);
        List<Player> players = new ArrayList<>();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                continue;
            }
            players.add(onlinePlayer);
        }
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();
        ClientboundAnimatePacket packet = new ClientboundAnimatePacket(serverPlayer, ClientboundAnimatePacket.SWING_MAIN_HAND);
        PacketUtils.sendPacket(players, packet);
    }

}
