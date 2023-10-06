package cz.jeme.programu.gungaming.loot.generator;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.game.Game;
import cz.jeme.programu.gungaming.loot.LootManager;
import cz.jeme.programu.gungaming.loot.crate.Crate;
import cz.jeme.programu.gungaming.util.Message;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public final class Task extends BukkitRunnable {
    private static final @NotNull Random RANDOM = new Random();
    private static final @NotNull DecimalFormat FORMATTER = new DecimalFormat("00.00");
    private final @NotNull Crate crate;
    private final @NotNull Audience audience;
    private final int xMin;
    private final int zMin;
    private final int xMax;
    private final int zMax;
    private boolean running = false;
    private boolean finished = false;
    private long tickStamp = -1;
    // How many blocks does the task render per tick
    // This number grows exponentially during the task execution, the initial value can be low
    private int blocksPerTick = 10;
    private int enlarge = 1;
    private int counter = 0;
    private int crateCounter = 0;
    private int x;
    private int z;
    private final int minHeight = Game.getWorld().getMinHeight();
    private final int maxHeight = Game.getWorld().getMaxHeight();

    Task(@NotNull Crate crate, @NotNull Audience audience, int xMin, int zMin, int xMax, int zMax) {
        this.crate = crate;
        this.audience = audience;
        this.xMin = xMin;
        this.xMax = xMax;
        this.zMin = zMin;
        this.zMax = zMax;
        x = xMin - 1;
        z = zMin;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void run() {
        if (tickStamp == -1) tickStamp = System.currentTimeMillis();
        long tickDuration = System.currentTimeMillis() - tickStamp;
        if (tickDuration > 50) {
            // The tick took longer than 50ms, that means that the task is overloading the server
            if (enlarge >= 0) { // When enlarging it overloads the server
                blocksPerTick -= enlarge; // Rollback last enlargement (the one that made the server overload)
                enlarge = -20; // Start reducing
            } else { // When reducing and it still overloads the server
                enlarge *= 2; // Double the reducing
            }
        } else { // The server is not overloaded
            if (enlarge <= 0) { // When reducing and it runs fine
                enlarge = 1; // Start enlarging
            } else { // When enlarging and it runs fine
                enlarge *= 2; // Enlarge the enlarging
            }
        }
        blocksPerTick = Math.max(1, blocksPerTick + enlarge); // Set the amount of blocks to check for this tick

        tickStamp = System.currentTimeMillis(); // Make a new tick stamp

        final int xSize = Math.abs(xMin - xMax);
        final int zSize = Math.abs(zMin - zMax);
        final float generatePercentage = Math.min(1f, (float) counter / (xSize * zSize));

        String color = tickDuration > 50 ? "<#FF0000>" : "<#00FF00>";
        String info = color + "[" + FORMATTER.format(generatePercentage * 100f)
                + "%] Generation: " + crate.getName()
                + "; Speed: " + blocksPerTick
                + "BPT; Tick duration: " + tickDuration + "ms"
                + Message.escape(color);
        audience.sendActionBar(Message.from(info));
        for (int i = 0; i < blocksPerTick; i++) {
            if (x == xMax) {
                if (z == zMax) {
                    cancel();
                    running = false;
                    finished = true;
                    Bukkit.getScheduler().runTask(GunGaming.getPlugin(), TaskManager.INSTANCE::check);
                    audience.sendMessage(Message.from(
                            "<green>Generated " + crateCounter
                                    + " instances of " + crate.getName() + "</green>"
                    ));
                    return;
                } else {
                    z++;
                }
                x = xMin;
            } else {
                x++;
            }

            counter++;
            if (RANDOM.nextFloat() > crate.getSpawnPercentage()) continue;
            crateCounter++;

            List<Integer> validHeights = new ArrayList<>();
            boolean lastBlockCollidable = false;
            for (int y = minHeight; y < maxHeight - 1; y++) {
                if (Game.getWorld().getBlockAt(x, y, z).getType().isCollidable()) {
                    lastBlockCollidable = true;
                } else if (lastBlockCollidable) {
                    validHeights.add(y);
                    lastBlockCollidable = false;
                }
            }

            final int y = validHeights.size() == 1 ? validHeights.get(0) : validHeights.get(RANDOM.nextInt(validHeights.size() - 1));
            final Block block = Game.getWorld().getBlockAt(x, y, z);
            block.setType(crate.getBlock());
            crate.getBlockAction().accept(block);
            generateCrate(block, crate);
        }
    }

    private void generateCrate(@NotNull Block block, @NotNull Crate crate) {
        String color = crate.getRarity().getColor();
        Inventory inventory = Bukkit.createInventory(
                null,
                InventoryType.CHEST,
                Message.from(color + crate.getName() + Message.escape(color))
        );

        int slot = 0;
        List<ItemStack> loot = LootManager.INSTANCE.generateLoot(crate, inventory.getSize());
        for (ItemStack item : loot) {
            inventory.setItem(slot, Objects.requireNonNullElseGet(item, () -> new ItemStack(Material.AIR)));
            slot++;
        }

        CrateGenerator.INSTANCE.addInventory(block, inventory);
    }

    public void start() {
        running = true;
        runTaskTimer(GunGaming.getPlugin(), 0L, 1L);
    }
}