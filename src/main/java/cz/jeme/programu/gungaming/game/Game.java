package cz.jeme.programu.gungaming.game;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.config.GameConfig;
import cz.jeme.programu.gungaming.config.GenerationConfig;
import cz.jeme.programu.gungaming.data.Data;
import cz.jeme.programu.gungaming.loot.crate.CrateGenerator;
import cz.jeme.programu.gungaming.util.Components;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minecraft.core.BlockPos;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.*;

public final class Game {
    public static final @NotNull Data<Byte, Boolean> FROZEN_DATA = Data.ofBoolean(GunGaming.namespaced("frozen"));
    public static final @NotNull Data<Byte, Boolean> GLIDING_DATA = Data.ofBoolean(GunGaming.namespaced("gliding"));
    public static final @NotNull Data<Byte, Boolean> INVULNERABLE_DATA = Data.ofBoolean(GunGaming.namespaced("invulnerable"));

    public static final @NotNull Sound START_SOUND = Sound.sound(GunGaming.namespaced("game.start"), Sound.Source.MASTER, 1, 1);
    public static final @NotNull Sound DING_SOUND = Sound.sound(GunGaming.namespaced("game.ding"), Sound.Source.MASTER, 1, 1);
    public static final @NotNull Sound DONG_SOUND = Sound.sound(GunGaming.namespaced("game.dong"), Sound.Source.MASTER, 1, 1);
    public static final @NotNull Sound WARNING_SOUND = Sound.sound(GunGaming.namespaced("game.warning"), Sound.Source.MASTER, 1, 1);
    public static final @NotNull Sound INFO_SOUND = Sound.sound(GunGaming.namespaced("game.info"), Sound.Source.MASTER, 1, 1);
    public static final @NotNull Sound END_SOUND = Sound.sound(GunGaming.namespaced("game.end"), Sound.Source.MASTER, 1, 1);

    private static @Nullable Game instance = null;

    @SuppressWarnings("UnstableApiUsage")
    private final @NotNull BossBar bossBar = BossBar.bossBar(
            Components.of("<b><#6786C8>Gun</#6786C8><#4C618D>Gaming</#4C618D> <#717B95>v"
                          + GunGaming.plugin().getPluginMeta().getVersion()
            ),
            1,
            BossBar.Color.BLUE,
            BossBar.Overlay.PROGRESS
    );

    private final @NotNull Audience audience;
    private final @NotNull Location audienceLocation;
    private final @NotNull World world;
    private final int duration = GameConfig.GAME_SECONDS.get();
    private final int size = GameConfig.SIZE.get();
    private final int teamPlayers = GameConfig.TEAM_PLAYERS.get();
    private final int centerX;
    private final int centerZ;
    private final int xMin;
    private final int zMin;
    private final int xMax;
    private final int zMax;
    private final @NotNull Location spawn;
    private final @NotNull Objective kills;
    private final @NotNull Scoreboard scoreboard;
    private final @NotNull List<BukkitRunnable> runnables = new ArrayList<>();
    private final @NotNull List<Player> players;
    private boolean gracePeriod = true;

    @SuppressWarnings("UnstableApiUsage")
    public Game(final @NotNull CommandSourceStack source) {
        this.audience = source.getSender();
        if (running()) {
            audience.sendMessage(Components.prefix("<red>A game is already running!"));
            throw new IllegalStateException("There can be only one instance of Game!");
        }
        players = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (players.size() % teamPlayers != 0) {
            audience.sendMessage(Components.prefix("<red>Not enough players for this team configuration!"));
            throw new IllegalStateException("Not enough players to create teams!");
        }
        if (players.size() / teamPlayers <= 1) {
            audience.sendMessage(Components.prefix("<red>There must be at least 2 teams to start a game!"));
            throw new IllegalStateException("Not enough players to start a game!");
        }
        if (players.size() / teamPlayers > GameTeam.TEAM_COUNT) {
            audience.sendMessage(Components.prefix("<red>Too many players for this team configuration!"));
            throw new IllegalStateException("Too many players to create teams!");
        }
        // init success, no issues found
        instance = this;

        this.audienceLocation = source.getLocation();
        this.world = audienceLocation.getWorld();

        final BlockPos centerPos = GameConfig.CENTER.get()
                .getBlockPos(((net.minecraft.commands.CommandSourceStack) source));
        centerX = centerPos.getX();
        centerZ = centerPos.getZ();

        spawn = new Location(world, centerX, 350, centerZ);

        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);
        world.setTime(GameConfig.WORLD_TIME.get());
        world.setClearWeatherDuration(duration * 20);
        world.getEntities().stream()
                .filter(entity -> entity.getType() != EntityType.PLAYER)
                .forEach(Entity::remove);
        final WorldBorder worldBorder = world.getWorldBorder();
        worldBorder.setCenter(centerX, centerZ);
        worldBorder.setSize(size);

        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        scoreboard.getObjectives().forEach(Objective::unregister);
        scoreboard.getTeams().forEach(Team::unregister);
        kills = scoreboard.registerNewObjective(
                GunGaming.namespaced("kills").asString(),
                Criteria.DUMMY,
                Components.of("<#00FFFF><b>" + Components.latinString("Kills"))
        );
        final List<GameTeam> teams = new ArrayList<>();
        for (int i = 0; i < players.size() / teamPlayers; i++) {
            final GameTeam team = GameTeam.byOrdinal(i);
            team.register(kills);
            teams.add(team);
        }
        Collections.shuffle(teams);
        Collections.shuffle(players);
        for (int i = 0; i < players.size(); i++) {
            final Player player = players.get(i);
            final GameTeam team = teams.get(i / teamPlayers);
            team.addPlayer(player);
        }

        for (final Player player : players) {
            player.closeInventory();
            player.spigot().respawn();
            player.setGameMode(GameMode.SPECTATOR);
            FROZEN_DATA.write(player, true);
            player.clearActivePotionEffects();
            player.getInventory().clear();
            player.setHealth(20);
            Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(20);
            player.setAbsorptionAmount(0);
            player.setFoodLevel(20);
            player.setExp(0);
            player.setLevel(0);
            player.activeBossBars().forEach(player::hideBossBar);
            player.showBossBar(bossBar);
            // clear advancements
            final Iterator<Advancement> advancements = Bukkit.advancementIterator();
            while (advancements.hasNext()) {
                final Advancement advancement = advancements.next();
                final AdvancementProgress progress = player.getAdvancementProgress(advancement);
                advancement.getCriteria().forEach(progress::revokeCriteria);
            }
        }

        xMin = centerX - size / 2;
        zMin = centerZ - size / 2;
        xMax = xMin + size;
        zMax = zMin + size;

        teleportPlayers();

        runnables.add(new Loading(this));

        CrateGenerator.INSTANCE.generate(
                audience,
                audienceLocation,
                xMin, zMin, xMax, zMax,
                GenerationConfig.BPS.get()
        );
    }

    public void teleportPlayers() {
        final int points = players.size() / teamPlayers;
        final int rowCount = (int) Math.ceil(Math.sqrt(points)); // how many rows will the shape have
        final int base = points / rowCount; // minimum amount of cols in a row
        final int extra = base + 1; // extra (maximum) amount of cols in a row
        int extraCount = points - base * rowCount; // amount of rows with extra length
        int baseCount = rowCount - extraCount; // amount of rows with base length
        final List<Integer> rows = new ArrayList<>();
        if (extraCount > baseCount) { // start with an extra row
            rows.add(base + 1);
            extraCount--;
        }
        while (baseCount + extraCount > 0) { // while there is still something to add
            if (baseCount > 0) {
                rows.add(base);
                baseCount--;
            }
            if (extraCount > 0) {
                rows.add(base + 1);
                extraCount--;
            }
        }
//        System.out.println();
//        for (final int row : rows) {
//            final String star = row == base && points - base * rowCount != 0 ? " *" : "* ";
//            System.out.println(star.repeat(row));
//        }
//        if (rows.stream().mapToInt(i -> i).sum() != points) throw new RuntimeException("Points do not match!");
        final Iterator<GameTeam> teamIterator = GameTeam.activeTeams().iterator();
        final double offsetZ = size / (rowCount + 1D);
        for (int rowId = 0; rowId < rows.size(); rowId++) {
            final int row = rows.get(rowId);
            final double z = zMin + (rowId + 1) * offsetZ;
            final double offsetX = size / (row + 1D);
            for (int colId = 0; colId < row; colId++) {
                final double x = xMin + (colId + 1) * offsetX;
                final Location location = new Location(world, x, 350, z);
                teamIterator.next().players().forEach(player -> player.teleport(location));
            }
        }
    }

    void loadingEnd() {
        kills.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (final Player player : players) {
            final GameTeam team = GameTeam.byPlayer(player);
            final List<Player> teammates = team.players().stream()
                    .filter(teammate -> !teammate.getUniqueId().equals(player.getUniqueId()))
                    .toList();
            final StringBuilder teammatesStr = new StringBuilder();
            for (int i = 0; i < teammates.size(); i++) {
                if (i == 0) teammatesStr.append("w/ ");
                final Player teammate = teammates.get(i);
                teammatesStr.append(teammate.getName());
                if (i != teammates.size() - 1) teammatesStr.append(", ");
            }
            player.showTitle(Title.title(
                    team.color().append(Components.of("Team " + team.displayName())),
                    team.color().append(Components.of(teammatesStr.toString())),
                    Title.Times.times(
                            Duration.ZERO,
                            Duration.ofSeconds(GameConfig.TEAM_ANNOUNCE_SECONDS.get() - 1),
                            Duration.ofSeconds(1)
                    )
            ));
        }
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                runnables.add(new StartCountdown(Game.this));
            }
        };
        runnable.runTaskLater(GunGaming.plugin(), GameConfig.TEAM_ANNOUNCE_SECONDS.get() * 20);
        runnables.add(runnable);
    }

    void startGame() {
        for (final Player player : players) {
            FROZEN_DATA.write(player, false);
            INVULNERABLE_DATA.write(player, true);
            GLIDING_DATA.write(player, true);
            player.setGameMode(GameMode.SURVIVAL);
            player.setGliding(true);
        }
        runnables.add(new GracePeriodCountdown(this));
    }

    void gracePeriodEnd() {
        gracePeriod = false;
        for (final Player player : players) {
            INVULNERABLE_DATA.write(player, false);
            GLIDING_DATA.write(player, false);
        }

        runnables.add(new GameCountdown(this, duration * 60 + 15));
        runnables.add(new AirDropRunnable(this));
        runnables.add(new RefillRunnable());
    }

    private static final @NotNull String GOLD = "<#D4AF37>";
    private static final @NotNull String SILVER = "<#C0C0C0>";
    private static final @NotNull String BRONZE = "<#D58E00>";
    private static final @NotNull String PEWTER = "<#E9EAEC>";

    private void stop() {
        instance = null;
        runnables.stream()
                .filter(runnable -> !runnable.isCancelled())
                .forEach(BukkitRunnable::cancel);
        GameTeam.unregisterAll();
        kills.unregister();
        CrateGenerator.INSTANCE.cancel(null);
        CrateGenerator.INSTANCE.removeCrates(null);
        for (final Player player : Bukkit.getOnlinePlayers()) {
            FROZEN_DATA.delete(player);
            INVULNERABLE_DATA.delete(player);
            GLIDING_DATA.delete(player);
            player.hideBossBar(bossBar);
        }
    }

    public void stopGame() {
        stop();
        for (final Player player : Bukkit.getOnlinePlayers()) {
            player.clearTitle();
        }
    }

    public void endGame() {
        if (players.isEmpty()) return; // just a precaution
        final Map<Integer, List<String>> scores = new HashMap<>();
        for (final GameTeam team : GameTeam.activeTeams()) {
            final int score = team.getScore();
            scores.putIfAbsent(score, new ArrayList<>());
            final String color = Components.toString(team.color());
            for (final Player player : team.players())
                scores.get(score).add(color + player.getName());
        }
        final List<Integer> sorted = new ArrayList<>(scores.keySet());
        sorted.sort(Collections.reverseOrder());
        final List<Component> messages = new ArrayList<>();
        messages.add(Components.of("<#00FF00>=== Game Finished ==="));
        if (!sorted.isEmpty()) {
            messages.add(Components.of(
                    GOLD + "<b>1"
                    + Components.latinString("st") + ": "
                    + String.join(GOLD + ", ", scores.get(sorted.getFirst()))));
            if (sorted.size() > 1) {
                messages.add(Components.of(
                        SILVER + "<b>2"
                        + Components.latinString("nd") + ": "
                        + String.join(SILVER + ", ", scores.get(sorted.get(1)))));
                if (sorted.size() > 2) {
                    messages.add(Components.of(
                            BRONZE + "<b>3"
                            + Components.latinString("rd") + ": "
                            + String.join(BRONZE + ", ", scores.get(sorted.get(1)))));
                }
            }
        }
        for (final Player player : players) {
            player.setGameMode(GameMode.SPECTATOR);
            messages.forEach(player::sendMessage);

            final int rank = sorted.indexOf(GameTeam.byPlayer(player).getScore()) + 1;
            final String rankStr = switch (rank) {
                case 1 -> GOLD + "1" + Components.latinString("st");
                case 2 -> SILVER + "2" + Components.latinString("nd");
                case 3 -> BRONZE + "3" + Components.latinString("rd");
                default -> PEWTER + rank + Components.latinString("th");
            };
            player.showTitle(Title.title(
                    Components.of("<b>" + rankStr),
                    Components.of("<gold>" + Components.latinString("You finished")),
                    Title.Times.times(Duration.ZERO, Duration.ofHours(1), Duration.ZERO)
            ));
            player.playSound(END_SOUND, player);
        }
        stop();
    }

    boolean removePlayer(final @NotNull Player player) {
        final boolean contained = players.remove(player);
        if (contained) GameTeam.byPlayer(player).removePlayer(player);
        if (GameTeam.activeTeams().size() <= 1) Bukkit.getScheduler().runTaskLater(
                GunGaming.plugin(),
                this::endGame,
                1L
        );
        return contained;
    }

    public static @NotNull Game instance() {
        return Objects.requireNonNull(instance, "Game is not running!");
    }

    public static boolean running() {
        return instance != null;
    }

    public @NotNull BossBar bossBar() {
        return bossBar;
    }

    public @NotNull World world() {
        return world;
    }

    public int duration() {
        return duration;
    }

    public int size() {
        return size;
    }

    public int centerX() {
        return centerX;
    }

    public int centerZ() {
        return centerZ;
    }

    public @NotNull Location spawn() {
        return spawn;
    }

    public int xMin() {
        return xMin;
    }

    public int zMin() {
        return zMin;
    }

    public int xMax() {
        return xMax;
    }

    public int zMax() {
        return zMax;
    }

    public boolean gracePeriod() {
        return gracePeriod;
    }
}
