package cz.jeme.programu.gungaming.loot;


import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.util.Messages;
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
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.text.DecimalFormat;
import java.util.*;

public class CrateGenerator {
    private static final Random RANDOM = new Random();
    private static final DecimalFormat FORMATTER = new DecimalFormat("#0.00");
    private static final HashMap<Crate, List<int[]>> CRATES = new HashMap<>();

    public static void generate(Crate crate, Location loc1, Location loc2, float percentage, Player player) {
        CRATES.put(crate, new ArrayList<>());

        final World world = loc1.getWorld();

        final int xMin = Math.min(loc1.getBlockX(), loc2.getBlockX());
        final int zMin = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        final int xMax = Math.max(loc1.getBlockX(), loc2.getBlockX());
        final int zMax = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        final int xSize = xMax - xMin;
        final int zSize = zMax - zMin;

        new BukkitRunnable() {
            int blocksCounter = 0;
            long tickStartStamp = System.currentTimeMillis();
            int blocksPerSecond = 500;
            int BPSEnlarger = 1;
            @Override
            public void run() {
                if (System.currentTimeMillis() - tickStartStamp > 50) {
                    blocksPerSecond--;
                    BPSEnlarger = 1;
                } else {
                    blocksPerSecond += BPSEnlarger;
                    BPSEnlarger *= 2;
                }
                tickStartStamp = System.currentTimeMillis();
                for (int i = 0; i < blocksPerSecond; i++) {
                    if (blocksCounter == xSize * zSize) { // All the blocks were checked
                        cancel();
                        player.sendMessage(Messages.from(crate.toString() + ": " + CRATES.get(crate).size()));
                        CRATES.put(crate, Collections.unmodifiableList(CRATES.get(crate)));
                        return;
                    }

                    blocksCounter++;

                    if (RANDOM.nextFloat() * 100 > percentage) continue;
                    final int x = RANDOM.nextInt(xMax - xMin) + xMin;
                    final int z = RANDOM.nextInt(zMax - zMin) + zMin;
                    final int y = world.getHighestBlockYAt(x, z);
                    final Block block = world.getBlockAt(x, y + 1, z);
                    block.setType(Material.BARREL);
                    crate(block, crate);
                    CRATES.get(crate).add(new int[]{x, block.getY(), z});
                }
                float p = blocksCounter / ((xSize * zSize) / 100f);
                player.sendActionBar(Messages.from(FORMATTER.format(p) + "% generated " + blocksPerSecond));
            }
        }.runTaskTimer(GunGaming.getPlugin(), 0L, 1L);
    }

    private static void crate(Block block, Crate crate) {
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
