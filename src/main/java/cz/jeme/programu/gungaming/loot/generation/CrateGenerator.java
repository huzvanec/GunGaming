package cz.jeme.programu.gungaming.loot.generation;


import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.loot.Crate;
import cz.jeme.programu.gungaming.loot.Loot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.*;

public enum CrateGenerator {
    INSTANCE;
    final @NotNull Map<Crate, List<int[]>> CRATES = new HashMap<>();
    @Nullable CrateGenerationTask generationTask = null;
    public void generateCrates(@NotNull Map<Crate, Float> crateMap, @NotNull Location loc1,
                               @NotNull Location loc2, @NotNull Player player) {
        if (generationTask != null) return;
        final World world = loc1.getWorld();

        final int xMin = Math.min(loc1.getBlockX(), loc2.getBlockX());
        final int zMin = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        final int xMax = Math.max(loc1.getBlockX(), loc2.getBlockX());
        final int zMax = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        new BukkitRunnable() {
            private int crateId = 0;
            private final List<Crate> crateList = new ArrayList<>(crateMap.keySet());

            @Override
            public void run() {
                if (generationTask != null) return;

                Crate crate = crateList.get(crateId);
                CRATES.put(crate, new ArrayList<>());

                generationTask = new CrateGenerationTask(crate, xMin, zMin, xMax, zMax, player, crateMap.get(crate), world);
                generationTask.runTaskTimer(GunGaming.getPlugin(), 0L, 1L);

                crateId++;
                if (crateId == crateList.size()) {
                    cancel();
                }
            }
        }.runTaskTimer(GunGaming.getPlugin(), 0L, 1L);
    }

    void initCrate(@NotNull Block block, @NotNull Crate crate) {
        if (!(block.getState() instanceof InventoryHolder inventoryHolder)) return;

        BlockDisplay blockDisplay = block.getWorld().spawn(block.getLocation(), BlockDisplay.class);
        blockDisplay.setBlock(crate.material.createBlockData());
        final float enlarge = 0.001f; // To prevent z-fighting
        Transformation transformation = new Transformation(new Vector3f(-enlarge / 2), new AxisAngle4f(), new Vector3f(1 + enlarge), new AxisAngle4f());
        blockDisplay.setTransformation(transformation);
        blockDisplay.setBrightness(new Display.Brightness(15, 15));

        Inventory inventory = inventoryHolder.getInventory();
        int slot = 0;
        for (ItemStack item : Loot.generate(inventory.getSize(), crate)) {
            inventory.setItem(slot, Objects.requireNonNullElseGet(item, () -> new ItemStack(Material.AIR)));
            slot++;
        }
    }
}