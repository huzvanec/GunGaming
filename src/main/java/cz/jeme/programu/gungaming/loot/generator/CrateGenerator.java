package cz.jeme.programu.gungaming.loot.generator;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.loot.crate.Crate;
import cz.jeme.programu.gungaming.util.Packets;
import cz.jeme.programu.gungaming.util.registry.Crates;
import net.kyori.adventure.audience.Audience;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public enum CrateGenerator {
    INSTANCE;

    private final @NotNull Map<CrateLocation, Inventory> inventories = new HashMap<>();

    public void generate(@NotNull Audience audience, int x1, int z1, int x2, int z2) {
        Crates.crates.values().forEach(c -> generate(c, audience, x1, z1, x2, z2));
    }

    private void generate(@NotNull Crate crate, @NotNull Audience audience, int x1, int z1, int x2, int z2) {
        final int xMin = Math.min(x1, x2);
        final int zMin = Math.min(z1, z2);

        final int xMax = Math.max(x1, x2);
        final int zMax = Math.max(z1, z2);

        TaskManager.INSTANCE.addTask(new Task(crate, audience, xMin, zMin, xMax, zMax));
    }

    void addInventory(@NotNull CrateLocation crateLocation, @NotNull Inventory inventory) {
        inventories.put(crateLocation, inventory);
    }

    void addInventory(@NotNull Block block, @NotNull Inventory inventory) {
        addInventory(translateCoords(block), inventory);
    }

    private static @NotNull CrateLocation translateCoords(@NotNull Block block) {
        return new CrateLocation(block.getX(), block.getY(), block.getZ());
    }

    public boolean showInventory(@NotNull CrateLocation crateLocation, @NotNull HumanEntity human) {
        if (!inventories.containsKey(crateLocation)) return false;
        Packets.sendPacketToAll(new ClientboundAnimatePacket(
                ((CraftHumanEntity) human).getHandle(),
                ClientboundAnimatePacket.SWING_MAIN_HAND
        ));
        Bukkit.getScheduler().runTaskLater(
                GunGaming.getPlugin(),
                () -> human.openInventory(inventories.get(crateLocation)),
                2L
        );
        return true;
    }

    public boolean showInventory(@NotNull Block block, @NotNull HumanEntity human) {
        return showInventory(translateCoords(block), human);
    }
}
