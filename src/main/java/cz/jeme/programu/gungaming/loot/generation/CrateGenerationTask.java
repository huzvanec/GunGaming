package cz.jeme.programu.gungaming.loot.generation;

import cz.jeme.programu.gungaming.loot.Crate;
import cz.jeme.programu.gungaming.util.Message;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class CrateGenerationTask extends BukkitRunnable {
    private static final @NotNull Random RANDOM = new Random();
    private static final @NotNull DecimalFormat FORMATTER = new DecimalFormat("#0.00");
    private final @NotNull CrateGenerator crateGenerator = CrateGenerator.INSTANCE;
    private int blocksCounter = 0;
    private long tickStartStamp = System.currentTimeMillis();
    private int blocksPerSecond = 10;
    private int BPSEnlarger = 1;
    private final Crate crate;
    private final int xMin;
    private final int zMin;
    private final int xMax;
    private final int zMax;
    private final int zSize;
    private final int xSize;
    private final @NotNull Player player;
    private final float percentage;
    private final @NotNull World world;

    public CrateGenerationTask(@NotNull Crate crate, int xMin, int zMin, int xMax, int zMax,
                               @NotNull Player player, float percentage, @NotNull World world) {
        this.crate = crate;
        this.xMin = xMin;
        this.zMin = zMin;
        this.xMax = xMax;
        this.zMax = zMax;
        this.player = player;
        this.percentage = percentage;
        this.world = world;

        xSize = xMax - xMin;
        zSize = zMax - zMin;
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() - tickStartStamp > 50) {
            if (BPSEnlarger >= 0) {
                blocksPerSecond -= BPSEnlarger;
                BPSEnlarger = -1;
            } else {
                BPSEnlarger *= 2;
            }
        } else {
            if (BPSEnlarger <= 0) {
                BPSEnlarger = 1;
            } else {
                BPSEnlarger *= 2;
            }
        }
        blocksPerSecond += BPSEnlarger;

        tickStartStamp = System.currentTimeMillis();
        for (int i = 0; i < blocksPerSecond; i++) {
            if (blocksCounter == xSize * zSize) { // All the blocks were checked
                cancel();
                String finishInfo = "<#0CFF00>Generated " + crateGenerator.CRATES.get(crate).size() + " crates of type " + crate + "</#0CFF00>";
                player.sendMessage(Message.from(finishInfo));
                player.sendActionBar(Message.from(""));
                crateGenerator.CRATES.put(crate, Collections.unmodifiableList(crateGenerator.CRATES.get(crate)));
                if (crateGenerator.generationTask == this) {
                    crateGenerator.generationTask = null;
                } else {
                    throw new IllegalStateException("CrateGeneration task was not registered in task variable of CrateGenerator!");
                }
                return;
            }

            blocksCounter++;

            if (RANDOM.nextFloat() * 100 > percentage) continue;
            final int x = RANDOM.nextInt(xMax - xMin) + xMin;
            final int z = RANDOM.nextInt(zMax - zMin) + zMin;

            List<Integer> ys = new ArrayList<>();
            boolean lastAir = false;
            for (int y = world.getMinHeight(); y < world.getMaxHeight() - 1; y++) {
                if (world.getBlockAt(x, y, z).getType().isAir()) {
                    if (!lastAir) {
                        ys.add(y);
                        lastAir = true;
                    }
                } else {
                    lastAir = false;
                }
            }

            final int y;
            if (ys.size() == 1) {
                y = ys.get(0);
            } else {
                y = ys.get(RANDOM.nextInt(ys.size() - 1));
            }
            System.out.println(ys.size() + " - x: " + x + " z: " + z);
            final Block block = world.getBlockAt(x, y, z);
            block.setType(Material.BARREL);
            crateGenerator.initCrate(block, crate);
            crateGenerator.CRATES.get(crate).add(new int[]{x, block.getY(), z});
        }
        float generatePercentage = blocksCounter / ((xSize * zSize) / 100f);
        String info = "<transition:#FF0000:#0CFF00:" + generatePercentage / 100f + ">[" + FORMATTER.format(generatePercentage) + "%] Generation: " + crate + "; Speed: " + blocksPerSecond + "b/s</transition>";
        player.sendActionBar(Message.from(info));
    }
}
