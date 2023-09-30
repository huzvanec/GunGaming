package cz.jeme.programu.gungaming.game;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.util.Message;
import cz.jeme.programu.gungaming.util.Sounds;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.List;
import java.util.Set;

public final class Game {
    private static @Nullable Game game;
    private final int size;
    private final int centerX;
    private final int centerZ;
    private final @NotNull CommandSender sender;
    private final @NotNull List<Player> players;
    private static @Nullable World world;
    private static final @NotNull BossBar BOSS_BAR = BossBar.bossBar(
            Message.from("<b><#6786C8>Gun</#6786C8><#4C618D>Gaming</#4C618D> <#717B95>v"
                    + GunGaming.getPlugin().getPluginMeta().getVersion()
                    + "</#717B95></b>"
            ),
            1f,
            BossBar.Color.BLUE,
            BossBar.Overlay.PROGRESS
    );

    public static final @NotNull Set<CreatureSpawnEvent.SpawnReason> DISABLED_SPAWNING = Set.of(
            CreatureSpawnEvent.SpawnReason.NATURAL,
            CreatureSpawnEvent.SpawnReason.LIGHTNING,
            CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN,
            CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM,
            CreatureSpawnEvent.SpawnReason.BUILD_WITHER,
            CreatureSpawnEvent.SpawnReason.NETHER_PORTAL,
            CreatureSpawnEvent.SpawnReason.SILVERFISH_BLOCK,
            CreatureSpawnEvent.SpawnReason.ENDER_PEARL,
            CreatureSpawnEvent.SpawnReason.RAID,
            CreatureSpawnEvent.SpawnReason.PATROL,
            CreatureSpawnEvent.SpawnReason.BEEHIVE
    );

    private Game(int size, int centerX, int centerZ, @NotNull CommandSender sender) {
        this.sender = sender;
        this.size = size;
        this.centerX = centerX;
        this.centerZ = centerZ;

        players = List.copyOf(Bukkit.getOnlinePlayers());

        getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        getWorld().setTime(1000);
        getWorld().setClearWeatherDuration(1);

        getWorld().getEntities().stream()
                .filter(e -> e.getType() != EntityType.PLAYER)
                .forEach(Entity::remove);

        WorldBorder worldBorder = getWorld().getWorldBorder();
        worldBorder.setCenter(centerX, centerZ);
        worldBorder.setSize(size);

        for (Player player : players) {
            Location location = player.getLocation();
            location.setWorld(getWorld());
            player.teleport(location.set(centerX, 330, centerZ));
            player.setGameMode(GameMode.SPECTATOR);
            Namespace.FROZEN.set(player, true);
            player.clearActivePotionEffects();
            player.getInventory().clear();
            player.setHealth(20D);
            player.setAbsorptionAmount(0);
            player.setFoodLevel(20);
            player.setExp(0);
            player.setLevel(0);
            player.activeBossBars().forEach(player::hideBossBar);
            player.showBossBar(BOSS_BAR);
        }

        new StartCountdown();
    }

    private void startGame() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Namespace.FROZEN.set(player, false);
            player.setGameMode(GameMode.SURVIVAL);
            player.setGliding(true);
            Namespace.GLIDING.set(player, true);
            Namespace.INVULNERABLE.set(player, true);
        }
        new Counter();
    }

    public static void onEntityToggleGlide(@NotNull EntityToggleGlideEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        Boolean gliding = Namespace.GLIDING.get(player);
        if (gliding == null || !gliding) return;
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate != null && chestplate.getType() == Material.ELYTRA) return;
        Material below = player.getWorld().getBlockAt(player.getLocation().subtract(0, 0.4, 0)).getType();
        if (below.isCollidable() || below == Material.LAVA || below == Material.WATER || below == Material.BUBBLE_COLUMN) {
            Namespace.GLIDING.set(player, false);
            Bukkit.getScheduler().runTaskLater(
                    GunGaming.getPlugin(),
                    () -> Namespace.INVULNERABLE.set(player, false),
                    20L * 3L
            );
            return;
        }
        event.setCancelled(true);
    }

    public static boolean isRunning() {
        return game != null;
    }

    public static @Nullable Game newInstance(int size, int centerX, int centerZ, @NotNull CommandSender sender) {
        if (isRunning()) return null;
        game = new Game(size, centerX, centerZ, sender);
        return game;
    }

    private class StartCountdown extends BukkitRunnable {
        private static final int COUNTER_START = 5;
        private int counter = COUNTER_START;

        private StartCountdown() {
            runTaskTimer(GunGaming.getPlugin(), 0L, 20L);
        }

        @Override
        public void run() {
            Title title;
            Sound sound;
            if (counter == 0) {
                cancel();
                title = Title.title(
                        Message.from("<#FF00FF>Good Luck!</#FF00FF>"),
                        Message.from("<gold>Game is starting...</gold>"),
                        Title.Times.times(Duration.ZERO, Duration.ofMillis(500), Duration.ofSeconds(1))
                );
                sound = Sounds.getSound("game.start", 1);
                startGame();
            } else {
                title = Title.title(
                        Message.from(
                                "<transition:#FF0000:#00FF00:"
                                        + (float) counter / COUNTER_START
                                        + ">" + counter
                                        + "</transition>"),
                        Message.from("<gold>Game is starting...</gold>"),
                        Title.Times.times(Duration.ZERO, Duration.ofSeconds(3), Duration.ZERO)
                );
                sound = Sounds.getSound("game.dong", 1);
            }

            for (Player player : players) {
                player.showTitle(title);
                player.playSound(sound, player);
            }
            counter--;
        }
    }

    private static class Counter extends BukkitRunnable {
        private static final long GAME_DURATION = 20 * 60; // Game duration in seconds
        private long counter = GAME_DURATION;
        private static final DecimalFormat FORMATTER = new DecimalFormat("00");

        public Counter() {
            runTaskTimer(GunGaming.getPlugin(), 0L, 20L);
        }

        @Override
        public void run() {
            float phase = (float) counter / GAME_DURATION;
            BOSS_BAR.progress(phase);
            BOSS_BAR.name(Message.from("<b><transition:#FF0000:#00FF00:" + phase + ">"
                    + translateTime(counter) + "</transition></b>"
            ));
            counter--;
        }

        private static String translateTime(long seconds) {
            long hours = seconds / 3600;
            seconds -= hours * 3600;
            long minutes = seconds / 60;
            seconds -= minutes * 60;
            String time = FORMATTER.format(minutes) + ":" + FORMATTER.format(seconds);
            if (hours != 0) {
                time = FORMATTER.format(hours) + ":" + time;
            }
            return time;
        }
    }

    public static @NotNull World getWorld() {
        if (world == null) {
            world = Bukkit.getWorlds().stream()
                    .filter(w -> w.getEnvironment() == World.Environment.NORMAL)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No overworld found!"));
        }
        return world;
    }
}
