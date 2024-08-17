package cz.jeme.programu.gungaming.game;

import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.config.GameConfig;
import cz.jeme.programu.gungaming.config.GenerationConfig;
import cz.jeme.programu.gungaming.data.Data;
import cz.jeme.programu.gungaming.game.lobby.Lobby;
import cz.jeme.programu.gungaming.game.runnable.AirDropRunnable;
import cz.jeme.programu.gungaming.game.runnable.GameRunnable;
import cz.jeme.programu.gungaming.game.runnable.Loading;
import cz.jeme.programu.gungaming.game.runnable.RefillRunnable;
import cz.jeme.programu.gungaming.game.runnable.countdown.GameCountdown;
import cz.jeme.programu.gungaming.game.runnable.countdown.GracePeriodCountdown;
import cz.jeme.programu.gungaming.game.runnable.countdown.StartCountdown;
import cz.jeme.programu.gungaming.item.tracker.TeammateTracker;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.*;

public final class Game {
    public static final @NotNull Data<Byte, Boolean> FROZEN_DATA = Data.ofBoolean(GunGaming.namespaced("frozen"));
    public static final @NotNull Data<Byte, Boolean> GLIDING_DATA = Data.ofBoolean(GunGaming.namespaced("gliding"));
    public static final @NotNull Data<Byte, Boolean> INVULNERABLE_DATA = Data.ofBoolean(GunGaming.namespaced("invulnerable"));

    public static final @NotNull Sound START_SOUND = Sound.sound(GunGaming.namespaced("game.start"), Sound.Source.MASTER, 1, 1);
    public static final @NotNull Sound DING_SOUND = Sound.sound(GunGaming.namespaced("game.ping.ding"), Sound.Source.MASTER, 1, 1);
    public static final @NotNull Sound DONG_SOUND = Sound.sound(GunGaming.namespaced("game.ping.dong"), Sound.Source.MASTER, 1, 1);
    public static final @NotNull Sound WARNING_SOUND = Sound.sound(GunGaming.namespaced("game.ping.warning"), Sound.Source.MASTER, 1, 1);
    public static final @NotNull Sound INFO_SOUND = Sound.sound(GunGaming.namespaced("game.ping.info"), Sound.Source.MASTER, 1, 1);
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
    private final int teamPlayerCount = GameConfig.TEAM_PLAYERS.get();
    private final int centerX;
    private final int centerZ;
    private final int xMin;
    private final int zMin;
    private final int xMax;
    private final int zMax;
    private final @NotNull Location spawn;
    private final @NotNull Objective kills;
    private final @NotNull Scoreboard scoreboard;
    private final @NotNull List<Player> players;
    private boolean gracePeriod = true;

    @SuppressWarnings("UnstableApiUsage")
    public Game(final @NotNull CommandSourceStack source) {
        this.audience = source.getSender();
        // error handling
        if (running()) {
            audience.sendMessage(Components.prefix("<red>A game is already running!"));
            throw new IllegalStateException("There can be only one instance of Game!");
        }
        players = new ArrayList<>(Bukkit.getOnlinePlayers());
        // players that should be auto-assigned to teams
        final Map<Player, GameTeam> overrideTeamPlayers = new HashMap<>();
        final List<Player> autoTeamPlayers = new ArrayList<>(players);
        // resort players into auto and override
        final Iterator<Player> autoTeamPlayerIterator = autoTeamPlayers.iterator();
        while (autoTeamPlayerIterator.hasNext()) {
            final Player player = autoTeamPlayerIterator.next();
            final GameTeam overriden = GameTeam.overrideRegistry().get(player.getUniqueId());
            if (overriden == null) continue;
            autoTeamPlayerIterator.remove();
            overrideTeamPlayers.put(player, overriden);
        }
        if (autoTeamPlayers.size() % teamPlayerCount != 0) {
            audience.sendMessage(Components.prefix("<red>Not enough players for this team configuration!"));
            throw new IllegalStateException("Not enough players to create teams!");
        }
        final int teamCount = autoTeamPlayers.size() / teamPlayerCount + GameTeam.overrideTeams().size();
        if (teamCount <= 1) {
            audience.sendMessage(Components.prefix("<red>There must be at least 2 teams to start a game!"));
            throw new IllegalStateException("Not enough teams to start a game!");
        }
        if (teamCount > GameTeam.COUNT) {
            audience.sendMessage(Components.prefix("<red>Too many players for this team configuration!"));
            throw new IllegalStateException("Too many players to create teams!");
        }
        // init success, no issues found
        instance = this;
        audience.sendMessage(Components.prefix("<green>Game started successfully"));

        if (Lobby.enabled()) Lobby.instance().disable();

        this.audienceLocation = source.getLocation();
        this.world = audienceLocation.getWorld();

        final BlockPos centerPos = GameConfig.CENTER.get()
                .getBlockPos(((net.minecraft.commands.CommandSourceStack) source));
        centerX = centerPos.getX();
        centerZ = centerPos.getZ();

        spawn = new Location(world, centerX, 350, centerZ);
        worldSetup(world);
        final WorldBorder worldBorder = world.getWorldBorder();
        worldBorder.setCenter(centerX, centerZ);
        worldBorder.setSize(size);
        // setting up scoreboard
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        scoreboard.getObjectives().forEach(Objective::unregister);
        scoreboard.getTeams().forEach(Team::unregister);
        kills = scoreboard.registerNewObjective(
                GunGaming.namespaced("kills").asString(),
                Criteria.DUMMY,
                Components.of("<#00FFFF><b>" + Components.latinString("Kills"))
        );
        // setting up teams
        GameTeam.overrideTeams().forEach(team -> team.register(kills)); // register overriden teams
        // register overriden players
        for (final Map.Entry<Player, GameTeam> entry : overrideTeamPlayers.entrySet()) {
            entry.getValue().addPlayer(entry.getKey());
        }
        // generate auto teams
        final List<GameTeam> autoTeams = new ArrayList<>();
        int o = 0;
        while (autoTeams.size() < autoTeamPlayers.size() / teamPlayerCount) {
            final GameTeam team = GameTeam.ofOrdinal(o++);
            if (team.hasOverrides()) continue;
            team.register(kills);
            autoTeams.add(team);
        }
        Collections.shuffle(autoTeams);
        Collections.shuffle(autoTeamPlayers);
        for (int i = 0; i < autoTeamPlayers.size(); i++) {
            final Player player = autoTeamPlayers.get(i);
            final GameTeam team = autoTeams.get(i / teamPlayerCount);
            team.addPlayer(player);
        }

        final ItemStack teammateTracker = CustomElement.of(TeammateTracker.class).item();

        // player init
        for (final Player player : players) {
            playerSetup(player);
            player.setGameMode(GameMode.SPECTATOR);
            FROZEN_DATA.write(player, true);
            player.showBossBar(bossBar);
            player.setScoreboard(scoreboard);
            if (GameTeam.ofPlayer(player).size() > 1) player.getInventory().setItem(8, teammateTracker);
        }

        xMin = centerX - size / 2;
        zMin = centerZ - size / 2;
        xMax = xMin + size;
        zMax = zMin + size;

        teleportPlayers();

        announceTeams();

        new GameRunnable() {
            @Override
            public void run() {
                generateCrates();
            }
        }.runTaskLater(GunGaming.plugin(), GameConfig.TEAM_ANNOUNCE_SECONDS.get() * 20);
    }

    @ApiStatus.Internal
    public static void worldSetup(final @NotNull World world) {
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);
        world.setGameRule(GameRule.DO_TILE_DROPS, true);
        world.setGameRule(GameRule.DO_FIRE_TICK, false);
        world.setTime(GameConfig.WORLD_TIME.get());
        world.setClearWeatherDuration(99);
        world.getEntities().stream()
                .filter(entity -> entity.getType() != EntityType.PLAYER)
                .forEach(Entity::remove);
        world.setDifficulty(Difficulty.NORMAL);
    }

    @ApiStatus.Internal
    public static void playerSetup(final @NotNull Player player) {
        player.spigot().respawn();
        player.clearTitle();
        player.closeInventory();
        player.clearActivePotionEffects();
        final PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setHeldItemSlot(0);
        player.setHealth(20);
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(20);
        player.setAbsorptionAmount(0);
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);
        player.activeBossBars().forEach(player::hideBossBar);
        player.undiscoverRecipes(player.getDiscoveredRecipes());
        // clear advancements
        final Iterator<Advancement> advancements = Bukkit.advancementIterator();
        while (advancements.hasNext()) {
            final Advancement advancement = advancements.next();
            final AdvancementProgress progress = player.getAdvancementProgress(advancement);
            advancement.getCriteria().forEach(progress::revokeCriteria);
        }
    }

    private void generateCrates() {
        CrateGenerator.INSTANCE.generate(
                audience,
                audienceLocation,
                xMin, zMin, xMax, zMax,
                GenerationConfig.BPS.get()
        );
        new Loading(this);
    }

    public void teleportPlayers() {
        final int points = GameTeam.activeTeams().size();
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
/*
        System.out.println();
        for (final int row : rows) {
            final String star = row == base && points - base * rowCount != 0 ? " *" : "* ";
            System.out.println(star.repeat(row));
        }
        if (rows.stream().mapToInt(i -> i).sum() != points) throw new RuntimeException("Points do not match!");
*/
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

    private void announceTeams() {
        kills.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (final Player player : players) {
            final GameTeam team = GameTeam.ofPlayer(player);
            final List<Player> teammates = team.players().stream()
                    .filter(p -> !p.getUniqueId().equals(player.getUniqueId()))
                    .toList();
            final StringBuilder teammatesStrB = new StringBuilder();
            for (int i = 0; i < teammates.size(); i++) {
                if (i == 0) teammatesStrB.append("w/ ");
                final Player teammate = teammates.get(i);
                teammatesStrB.append(teammate.getName());
                if (i != teammates.size() - 1) teammatesStrB.append(", ");
            }
            player.playSound(INFO_SOUND, player);
            player.showTitle(Title.title(
                    team.color().append(Components.of("Team " + team.displayName())),
                    team.color().append(Components.of(teammatesStrB.toString())),
                    Title.Times.times(
                            Duration.ZERO,
                            Duration.ofSeconds(GameConfig.TEAM_ANNOUNCE_SECONDS.get() - 1),
                            Duration.ofSeconds(1)
                    )
            ));
        }
    }

    @ApiStatus.Internal
    public void loadingEnd() {
        new StartCountdown(Game.this);
    }

    @ApiStatus.Internal
    public void startGame() {
        for (final Player player : players) {
            FROZEN_DATA.write(player, false);
            INVULNERABLE_DATA.write(player, true);
            GLIDING_DATA.write(player, true);
            player.setGameMode(GameMode.SURVIVAL);
            player.setGliding(true);
        }
        new GracePeriodCountdown(this);
    }

    @ApiStatus.Internal
    public void gracePeriodEnd() {
        gracePeriod = false;
        for (final Player player : players) {
            INVULNERABLE_DATA.write(player, false);
            GLIDING_DATA.write(player, false);
        }

        new GameCountdown(this);
        new AirDropRunnable(this);
        new RefillRunnable();
    }

    private void stop() {
        instance = null;
        GameRunnable.cancelAll();
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

    private static @NotNull String rankToColor(final int rank) {
        return switch (rank) {
            case 0 -> throw new IllegalArgumentException("Rank must be >= 1!");
            case 1 -> "<#D4AF37>"; // gold
            case 2 -> "<#C0C0C0>"; // silver
            case 3 -> "<#D58E00>"; // bronze
            default -> "<#E9EAEC>"; // pewter
        };
    }

    private static @NotNull Component parseRank(final int rank) {
        final String color = rankToColor(rank);
        return switch (rank) {
            case 1 -> Components.of(color + "1" + Components.latinString("st"));
            case 2 -> Components.of(color + "2" + Components.latinString("nd"));
            case 3 -> Components.of(color + "3" + Components.latinString("rd"));
            default -> Components.of(color + rank + Components.latinString("th"));
        };
    }

    public void endGame() {
        if (players.isEmpty()) return; // just a precaution
        // read team scores
        final Map<Integer, List<GameTeam>> scoreTeams = new TreeMap<>(Collections.reverseOrder());
        for (final GameTeam team : GameTeam.activeTeams()) {
            final int score = team.score();
            scoreTeams.putIfAbsent(score, new ArrayList<>());
            scoreTeams.get(score).add(team);
        }
        // send messages to the whole server
        final List<Component> messages = new ArrayList<>();
        messages.add(Components.of("<#00FF00>=== Game Finished ==="));
        int rank = 1;
        for (final Map.Entry<Integer, List<GameTeam>> entry : scoreTeams.entrySet()) {
            final int score = entry.getKey();
            final List<GameTeam> teams = entry.getValue();
            final Component rankComponent = parseRank(rank);

            final List<String> players = new ArrayList<>();
            for (final GameTeam team : teams) {
                final String color = Components.toString(team.color());
                final List<Player> teamPlayers = new ArrayList<>(team.players());
                // title start
                for (final Player player : teamPlayers) {
                    player.showTitle(Title.title(
                            Components.of("<b>").append(rankComponent),
                            Components.of("<gold>" + Components.latinString("You finished")),
                            Title.Times.times(Duration.ZERO, Duration.ofHours(1), Duration.ZERO)
                    ));
                }
                // title end
                teamPlayers.addAll(team.removedPlayers());
                teamPlayers.forEach(player -> {
                    final String playerScore = team.players().size() > 1 ? "[" + team.score(player) + "] " : "";
                    players.add(color + playerScore + player.getName());
                });
            }
            final String color = rankToColor(rank);
            messages.add(Components.of("<b>").append(rankComponent.append(Components.of(
                    "<!b> [" + score + "]: "
                    + String.join(color + ", ", players)
            ))));
            rank++;
        }
        for (final Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.SPECTATOR);
            messages.forEach(player::sendMessage);
            player.playSound(END_SOUND, player);
        }
        stop();
    }

    boolean removePlayer(final @NotNull Player player) {
        final boolean contained = players.remove(player);
        Bukkit.getScheduler().runTask(
                GunGaming.plugin(),
                () -> {
                    if (contained) {
                        final GameTeam team = GameTeam.ofPlayer(player);
                        if (team.removePlayer(player) && teamPlayerCount > 1)
                            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(
                                            Components.of("<#FF0000>❌ The ").append(
                                                    team.color().append(Components.of(
                                                            team.displayName()
                                                            + "<#FF0000> team got eliminated"
                                                    ))
                                            )
                                    )
                            );
                    }
                    if (GameTeam.activeTeams().size() <= 1) endGame();
                }
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
