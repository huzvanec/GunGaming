package cz.jeme.programu.gungaming.eventhandler.interaction;

import cz.jeme.programu.gungaming.Namespaces;
import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.loot.Crate;
import cz.jeme.programu.gungaming.manager.CooldownManager;
import cz.jeme.programu.gungaming.util.Materials;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.Packets;
import cz.jeme.programu.gungaming.util.item.Ammos;
import cz.jeme.programu.gungaming.util.item.Guns;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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
            player.sendActionBar(Messages.from("<red>Reload required!</red>"));
            return;
        }
        if (!isCreative) {
            Ammos.remove(heldItem, 1);
        }
        gun.shoot(event, heldItem);
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
        Packets.sendPacket(players, packet);
    }
}
